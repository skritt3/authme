//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package fr.xephi.authme.security.crypts;

import fr.xephi.authme.security.crypts.description.Recommendation;
import fr.xephi.authme.security.crypts.description.Usage;
import java.util.Arrays;

/** @deprecated */
@Deprecated
@Recommendation(Usage.DEPRECATED)
public class Whirlpool extends UnsaltedMethod {
  public static final int DIGESTBITS = 512;
  public static final int DIGESTBYTES = 64;
  protected static final int R = 10;
  private static final String sbox = "ᠣ웨螸ŏ㚦틵祯酒悼鮎ꌌ笵ᷠퟂ\u2e4b﹗ᕷ㟥\u9ff0䫚壉⤊놠殅뵝ჴ쬾է\ue427䆋Ᵹ闘ﯮ籦\udd17䞞쨭뼇굚茳挂ꩱ젙䧙\uf2e3守騦㊰\ue90f햀뻍㑈ｺ遟\u2068\u1aae둔錢擱猒䀈쏬\udba1贽需켫皂혛떯橐䗳ワ㽕ꋪ斺⿀\ude1c\ufd4d鉵ڊ닦ฟ拔ꢖ暈╙葲㥌幸㢌톥\ue261댡鰞䏇ﰄ写洍\ufadf縤㮫츑轎럫㲁铷뤓ⳓ\ue76e쐃噄義⪻셓\udc0b鵬ㅴ\uf646겉ᓡᘺ椉炶탭챂颤⡜\uf886";
  private static final long[][] C = new long[8][256];
  private static final long[] rc = new long[11];
  protected final byte[] bitLength = new byte[32];
  protected final byte[] buffer = new byte[64];
  protected int bufferBits = 0;
  protected int bufferPos = 0;
  protected final long[] hash = new long[8];
  protected final long[] K = new long[8];
  protected final long[] L = new long[8];
  protected final long[] block = new long[8];
  protected final long[] state = new long[8];

  public Whirlpool() {
  }

  protected static String display(byte[] array) {
    char[] val = new char[2 * array.length];
    String hex = "0123456789ABCDEF";

    for(int i = 0; i < array.length; ++i) {
      int b = array[i] & 255;
      val[2 * i] = hex.charAt(b >>> 4);
      val[2 * i + 1] = hex.charAt(b & 15);
    }

    return String.valueOf(val);
  }

  protected void processBuffer() {
    int r = 0;

    int i;
    for(i = 0; r < 8; i += 8) {
      this.block[r] = (long)this.buffer[i] << 56 ^ ((long)this.buffer[i + 1] & 255L) << 48 ^ ((long)this.buffer[i + 2] & 255L) << 40 ^ ((long)this.buffer[i + 3] & 255L) << 32 ^ ((long)this.buffer[i + 4] & 255L) << 24 ^ ((long)this.buffer[i + 5] & 255L) << 16 ^ ((long)this.buffer[i + 6] & 255L) << 8 ^ (long)this.buffer[i + 7] & 255L;
      ++r;
    }

    for(r = 0; r < 8; ++r) {
      this.state[r] = this.block[r] ^ (this.K[r] = this.hash[r]);
    }

    long[] var10000;
    for(r = 1; r <= 10; ++r) {
      int t;
      int s;
      for(i = 0; i < 8; ++i) {
        this.L[i] = 0L;
        t = 0;

        for(s = 56; t < 8; s -= 8) {
          var10000 = this.L;
          var10000[i] ^= C[t][(int)(this.K[i - t & 7] >>> s) & 255];
          ++t;
        }
      }

      for(i = 0; i < 8; ++i) {
        this.K[i] = this.L[i];
      }

      var10000 = this.K;
      var10000[0] ^= rc[r];

      for(i = 0; i < 8; ++i) {
        this.L[i] = this.K[i];
        t = 0;

        for(s = 56; t < 8; s -= 8) {
          var10000 = this.L;
          var10000[i] ^= C[t][(int)(this.state[i - t & 7] >>> s) & 255];
          ++t;
        }
      }

      for(i = 0; i < 8; ++i) {
        this.state[i] = this.L[i];
      }
    }

    for(r = 0; r < 8; ++r) {
      var10000 = this.hash;
      var10000[r] ^= this.state[r] ^ this.block[r];
    }

  }

  public void NESSIEinit() {
    Arrays.fill(this.bitLength, (byte)0);
    this.bufferBits = this.bufferPos = 0;
    this.buffer[0] = 0;
    Arrays.fill(this.hash, 0L);
  }

  public void NESSIEadd(byte[] source, long sourceBits) {
    int sourcePos = 0;
    int sourceGap = 8 - ((int)sourceBits & 7) & 7;
    int bufferRem = this.bufferBits & 7;
    long value = sourceBits;
    int i = 31;

    for(int carry = 0; i >= 0; --i) {
      carry += (this.bitLength[i] & 255) + ((int)value & 255);
      this.bitLength[i] = (byte)carry;
      carry >>>= 8;
      value >>>= 8;
    }

    int b;
    byte[] var10000;
    while(sourceBits > 8L) {
      b = source[sourcePos] << sourceGap & 255 | (source[sourcePos + 1] & 255) >>> 8 - sourceGap;
      if (b < 0 || b >= 256) {
        throw new RuntimeException("LOGIC ERROR");
      }

      var10000 = this.buffer;
      int var10003 = this.bufferPos++;
      var10000[var10003] = (byte)(var10000[var10003] | b >>> bufferRem);
      this.bufferBits += 8 - bufferRem;
      if (this.bufferBits == 512) {
        this.processBuffer();
        this.bufferBits = this.bufferPos = 0;
      }

      this.buffer[this.bufferPos] = (byte)(b << 8 - bufferRem & 255);
      this.bufferBits += bufferRem;
      sourceBits -= 8L;
      ++sourcePos;
    }

    if (sourceBits > 0L) {
      b = source[sourcePos] << sourceGap & 255;
      var10000 = this.buffer;
      int var10001 = this.bufferPos;
      var10000[var10001] = (byte)(var10000[var10001] | b >>> bufferRem);
    } else {
      b = 0;
    }

    if ((long)bufferRem + sourceBits < 8L) {
      this.bufferBits = (int)((long)this.bufferBits + sourceBits);
    } else {
      ++this.bufferPos;
      this.bufferBits += 8 - bufferRem;
      sourceBits -= (long)(8 - bufferRem);
      if (this.bufferBits == 512) {
        this.processBuffer();
        this.bufferBits = this.bufferPos = 0;
      }

      this.buffer[this.bufferPos] = (byte)(b << 8 - bufferRem & 255);
      this.bufferBits += (int)sourceBits;
    }

  }

  public void NESSIEfinalize(byte[] digest) {
    byte[] var10000 = this.buffer;
    int var10001 = this.bufferPos;
    var10000[var10001] = (byte)(var10000[var10001] | 128 >>> (this.bufferBits & 7));
    ++this.bufferPos;
    if (this.bufferPos > 32) {
      while(true) {
        if (this.bufferPos >= 64) {
          this.processBuffer();
          this.bufferPos = 0;
          break;
        }

        this.buffer[this.bufferPos++] = 0;
      }
    }

    while(this.bufferPos < 32) {
      this.buffer[this.bufferPos++] = 0;
    }

    System.arraycopy(this.bitLength, 0, this.buffer, 32, 32);
    this.processBuffer();
    int i = 0;

    for(int j = 0; i < 8; j += 8) {
      long h = this.hash[i];
      digest[j] = (byte)((int)(h >>> 56));
      digest[j + 1] = (byte)((int)(h >>> 48));
      digest[j + 2] = (byte)((int)(h >>> 40));
      digest[j + 3] = (byte)((int)(h >>> 32));
      digest[j + 4] = (byte)((int)(h >>> 24));
      digest[j + 5] = (byte)((int)(h >>> 16));
      digest[j + 6] = (byte)((int)(h >>> 8));
      digest[j + 7] = (byte)((int)h);
      ++i;
    }

  }

  public void NESSIEadd(String source) {
    if (source.length() > 0) {
      byte[] data = new byte[source.length()];

      for(int i = 0; i < source.length(); ++i) {
        data[i] = (byte)source.charAt(i);
      }

      this.NESSIEadd(data, (long)(8 * data.length));
    }

  }

  public String computeHash(String password) {
    byte[] digest = new byte[64];
    this.NESSIEinit();
    this.NESSIEadd(password);
    this.NESSIEfinalize(digest);
    return display(digest);
  }

  static {
    int x;
    for(x = 0; x < 256; ++x) {
      char c = "ᠣ웨螸ŏ㚦틵祯酒悼鮎ꌌ笵ᷠퟂ\u2e4b﹗ᕷ㟥\u9ff0䫚壉⤊놠殅뵝ჴ쬾է\ue427䆋Ᵹ闘ﯮ籦\udd17䞞쨭뼇굚茳挂ꩱ젙䧙\uf2e3守騦㊰\ue90f햀뻍㑈ｺ遟\u2068\u1aae둔錢擱猒䀈쏬\udba1贽需켫皂혛떯橐䗳ワ㽕ꋪ斺⿀\ude1c\ufd4d鉵ڊ닦ฟ拔ꢖ暈╙葲㥌幸㢌톥\ue261댡鰞䏇ﰄ写洍\ufadf縤㮫츑轎럫㲁铷뤓ⳓ\ue76e쐃噄義⪻셓\udc0b鵬ㅴ\uf646겉ᓡᘺ椉炶탭챂颤⡜\uf886".charAt(x / 2);
      long v1 = (x & 1) == 0 ? (long)(c >>> 8) : (long)(c & 255);
      long v2 = v1 << 1;
      if (v2 >= 256L) {
        v2 ^= 285L;
      }

      long v4 = v2 << 1;
      if (v4 >= 256L) {
        v4 ^= 285L;
      }

      long v5 = v4 ^ v1;
      long v8 = v4 << 1;
      if (v8 >= 256L) {
        v8 ^= 285L;
      }

      long v9 = v8 ^ v1;
      C[0][x] = v1 << 56 | v1 << 48 | v4 << 40 | v1 << 32 | v8 << 24 | v5 << 16 | v2 << 8 | v9;

      for(int t = 1; t < 8; ++t) {
        C[t][x] = C[t - 1][x] >>> 8 | C[t - 1][x] << 56;
      }
    }

    rc[0] = 0L;

    for(x = 1; x <= 10; ++x) {
      int i = 8 * (x - 1);
      rc[x] = C[0][i] & -72057594037927936L ^ C[1][i + 1] & 71776119061217280L ^ C[2][i + 2] & 280375465082880L ^ C[3][i + 3] & 1095216660480L ^ C[4][i + 4] & 4278190080L ^ C[5][i + 5] & 16711680L ^ C[6][i + 6] & 65280L ^ C[7][i + 7] & 255L;
    }

  }
}
