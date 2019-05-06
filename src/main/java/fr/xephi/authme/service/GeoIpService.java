package fr.xephi.authme.service;

import fr.xephi.authme.ConsoleLogger;
import fr.xephi.authme.initialization.DataFolder;
import fr.xephi.authme.libs.google.common.annotations.VisibleForTesting;
import fr.xephi.authme.libs.javax.inject.Inject;
import fr.xephi.authme.libs.maxmind.geoip.Country;
import fr.xephi.authme.libs.maxmind.geoip.LookupService;
import fr.xephi.authme.util.FileUtils;
import fr.xephi.authme.util.InternetProtocolUtils;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.concurrent.TimeUnit;
import java.util.zip.GZIPInputStream;

public class GeoIpService
{
  private static final String LICENSE = "[LICENSE] This product uses data from the GeoLite API created by MaxMind, available at http://www.maxmind.com";
  private static final String GEOIP_URL = "http://geolite.maxmind.com/download/geoip/database/GeoLiteCountry/GeoIP.dat.gz";
  private LookupService lookupService;
  private Thread downloadTask;
  private final File dataFile;
  
  @Inject
  GeoIpService(@DataFolder File dataFolder)
  {
    this.dataFile = new File(dataFolder, "GeoIP.dat");
    
    isDataAvailable();
  }
  
  @VisibleForTesting
  GeoIpService(@DataFolder File dataFolder, LookupService lookupService)
  {
    this.dataFile = dataFolder;
    this.lookupService = lookupService;
  }
  
  private synchronized boolean isDataAvailable()
  {
    if ((this.downloadTask != null) && (this.downloadTask.isAlive())) {
      return false;
    }
    if (this.lookupService != null) {
      return true;
    }
    if (this.dataFile.exists())
    {
      boolean dataIsOld = System.currentTimeMillis() - this.dataFile.lastModified() > TimeUnit.DAYS.toMillis(30L);
      if (!dataIsOld) {
        try
        {
          this.lookupService = new LookupService(this.dataFile, 1);
          ConsoleLogger.info("[LICENSE] This product uses data from the GeoLite API created by MaxMind, available at http://www.maxmind.com");
          return true;
        }
        catch (IOException e)
        {
          ConsoleLogger.logException("Failed to load GeoLiteAPI database", e);
          return false;
        }
      }
      FileUtils.delete(this.dataFile);
    }
    this.downloadTask = createDownloadTask();
    this.downloadTask.start();
    return false;
  }
  
  private Thread createDownloadTask()
  {
    return new Thread(new Runnable()
    {
      public void run()
      {
        try
        {
          URL downloadUrl = new URL("http://geolite.maxmind.com/download/geoip/database/GeoLiteCountry/GeoIP.dat.gz");
          URLConnection conn = downloadUrl.openConnection();
          conn.setConnectTimeout(10000);
          conn.connect();
          InputStream input = conn.getInputStream();
          if (conn.getURL().toString().endsWith(".gz")) {
            input = new GZIPInputStream(input);
          }
          OutputStream output = new FileOutputStream(GeoIpService.this.dataFile);
          byte[] buffer = new byte['ࠀ'];
          int length = input.read(buffer);
          while (length >= 0)
          {
            output.write(buffer, 0, length);
            length = input.read(buffer);
          }
          output.close();
          input.close();
        }
        catch (IOException e)
        {
          ConsoleLogger.logException("Could not download GeoLiteAPI database", e);
        }
      }
    });
  }
  
  public String getCountryCode(String ip)
  {
    if ((!InternetProtocolUtils.isLocalAddress(ip)) && (isDataAvailable())) {
      return this.lookupService.getCountry(ip).getCode();
    }
    return "--";
  }
  
  public String getCountryName(String ip)
  {
    if ((!InternetProtocolUtils.isLocalAddress(ip)) && (isDataAvailable())) {
      return this.lookupService.getCountry(ip).getName();
    }
    return "N/A";
  }
}


/* Location:              C:\Users\Leonid\Downloads\AuthMe-5_4_0_jarSave.jar!\fr\xephi\authme\service\GeoIpService.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */