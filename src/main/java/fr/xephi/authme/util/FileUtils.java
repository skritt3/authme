package fr.xephi.authme.util;

import fr.xephi.authme.AuthMe;
import fr.xephi.authme.ConsoleLogger;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.CopyOption;
import java.nio.file.Files;

public final class FileUtils
{
  public static boolean copyFileFromResource(File destinationFile, String resourcePath)
  {
    if (destinationFile.exists()) {
      return true;
    }
    if (!createDirectory(destinationFile.getParentFile()))
    {
      ConsoleLogger.warning("Cannot create parent directories for '" + destinationFile + "'");
      return false;
    }
    try
    {
      InputStream is = getResourceFromJar(resourcePath);Throwable localThrowable4 = null;
      try
      {
        if (is == null)
        {
          ConsoleLogger.warning(String.format("Cannot copy resource '%s' to file '%s': cannot load resource", new Object[] { resourcePath, destinationFile
            .getPath() }));
        }
        else
        {
          Files.copy(is, destinationFile.toPath(), new CopyOption[0]);
          return true;
        }
      }
      catch (Throwable localThrowable6)
      {
        localThrowable4 = localThrowable6;throw localThrowable6;
      }
      finally
      {
        if (is != null) {
          if (localThrowable4 != null) {
            try
            {
              is.close();
            }
            catch (Throwable localThrowable3)
            {
              localThrowable4.addSuppressed(localThrowable3);
            }
          } else {
            is.close();
          }
        }
      }
    }
    catch (IOException e)
    {
      ConsoleLogger.logException(String.format("Cannot copy resource '%s' to file '%s':", new Object[] { resourcePath, destinationFile
        .getPath() }), e);
    }
    return false;
  }
  
  public static boolean createDirectory(File dir)
  {
    if ((!dir.exists()) && (!dir.mkdirs()))
    {
      ConsoleLogger.warning("Could not create directory '" + dir + "'");
      return false;
    }
    return dir.isDirectory();
  }
  
  public static InputStream getResourceFromJar(String path)
  {
    String normalizedPath = path.replace("\\", "/");
    return AuthMe.class.getClassLoader().getResourceAsStream(normalizedPath);
  }
  
  public static void purgeDirectory(File directory)
  {
    if (!directory.isDirectory()) {
      return;
    }
    File[] files = directory.listFiles();
    if (files == null) {
      return;
    }
    for (File target : files)
    {
      if (target.isDirectory()) {
        purgeDirectory(target);
      }
      delete(target);
    }
  }
  
  public static void delete(File file)
  {
    if (file != null)
    {
      boolean result = file.delete();
      if (!result) {
        ConsoleLogger.warning("Could not delete file '" + file + "'");
      }
    }
  }
  
  public static void create(File file)
  {
    try
    {
      boolean result = file.createNewFile();
      if (!result) {
        throw new IllegalStateException("Could not create file '" + file + "'");
      }
    }
    catch (IOException e)
    {
      throw new IllegalStateException("Error while creating file '" + file + "'", e);
    }
  }
  
  public static String makePath(String... elements)
  {
    return String.join(File.separator, elements);
  }
}


/* Location:              C:\Users\Leonid\Downloads\AuthMe-5_4_0_jarSave.jar!\fr\xephi\authm\\util\FileUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */