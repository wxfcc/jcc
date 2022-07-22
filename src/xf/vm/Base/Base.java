package xf.jcc;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.lang.management.ManagementFactory;
import java.util.zip.Deflater;
import java.util.zip.GZIPInputStream;
import java.util.zip.Inflater;
import java.util.zip.InflaterInputStream;

public class Base {
    protected static String readTextFile(String filename){
    	return readTextFile(new File(filename));
    }
    protected static String readTextFile(File file){
    	if(!file.exists() || file.isDirectory()) {
    		return null;
    	}
    	FileInputStream fis;
        try{
        	fis = new FileInputStream(file);
            BufferedInputStream bis = new BufferedInputStream(fis);
            //file.length();
            
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte abyte0[] = new byte[4096];
            int i = 0, j;
            do{
                try{
                    j = bis.read(abyte0);
                }
                catch(IOException ioexception){
                    ioexception.printStackTrace();
                    j = i;
                }
                if(j > 0)
                	baos.write(abyte0, 0, j);
                i = j;
            } while(j == abyte0.length);
            fis.close();
            return baos.toString("UTF-8");
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }

    protected static byte[] readFile(File file, int max){
    	if(!file.exists() || file.isDirectory()) {
    		return null;
    	}
    	FileInputStream fis;
        try{
        	fis = new FileInputStream(file);
            BufferedInputStream bis = new BufferedInputStream(fis);
            
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte abyte0[] = new byte[4096];
            int readLen = 0, readCount = 0;
            if(max == 0)
            	max = (int)file.length();
            else if(max < 0)
            	max = 1024*128;						// just need read 128KB
            do{
                try{
                    readLen = bis.read(abyte0);
                    if(readLen > 0) {
                    	baos.write(abyte0, 0, readLen);
                    	readCount += readLen;
                    	if(readCount >= max)
                    		break;
                    }
                    else
                    	break;
                }
                catch(IOException e2){
                    e2.printStackTrace();
                    break;
                }
            } while(true);
            fis.close();
            return baos.toByteArray();
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }
    
	protected static void writeFile(String fn, byte[] data) {
		writeFile(fn, data, false);
	}
	protected static void writeFile(File f, byte[] data) {
		writeFile(f, data, false);
	}
	protected static void writeFile(File f, String content) {
		try {
			writeFile(f, content.getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	public static void writeFile(String filename, byte[] data, boolean append) {
		File f = new File(filename);
		writeFile(f, data, append);
	}
	public static void writeFile(File f, byte[] data, boolean append) {
		try {
			FileOutputStream os;
			os = new FileOutputStream(f, append);
			os.write(data);
			os.flush();
			os.close();
		} catch (Exception e) {
			println("writeFile e:"+e.toString());
		}
	}
    public static byte[] readFile(String filename){
    	return readFile(new File(filename), 0);
    }

	public static void writeFile(String filename, String s, boolean append) {
		try {
			byte[] data = s.getBytes("UTF-8");
			File logf = new File(filename);
			FileOutputStream os;
			os = new FileOutputStream(logf, append);
			os.write(data);
			os.flush();
			os.close();
		} catch (Exception e) {
			println("writeFile e:"+e.toString());
		}
	}
	public static byte[] gzipDecode(byte[] data, String filename) {
        try {
            ByteArrayInputStream bais = new ByteArrayInputStream(data);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            GZIPInputStream gzip = new GZIPInputStream(bais);
            if(gzip.available() > 0) {
	            byte[] encode = new byte[8192];
	            while(true) {
		            int len = gzip.read(encode);
		            if(len > 0)
		            	baos.write(encode, 0, len);
		            else
		            	break;
	            }
	            gzip.close();
	            bais.close();
	            byte[] srcdata = baos.toByteArray();
	            return srcdata;
            }
        } catch (Exception e) {
        	println("gzip decode failed: " + filename +", "+ e.toString());
//            e.printStackTrace();
        }

        return null;
    }
    public static byte[] decompress(byte[] data) {  
        byte[] output = new byte[0];  
  
        Inflater decompresser = new Inflater();  
        decompresser.reset();  
        decompresser.setInput(data);  
  
        ByteArrayOutputStream o = new ByteArrayOutputStream(data.length);  
        try {  
            byte[] buf = new byte[1024];  
            while (!decompresser.finished()) {  
                int i = decompresser.inflate(buf);  
                o.write(buf, 0, i);  
            }  
            output = o.toByteArray();  
        } catch (Exception e) {  
            output = data;  
            e.printStackTrace();  
        } finally {  
            try {  
                o.close();  
            } catch (IOException e) {  
                e.printStackTrace();  
            }  
        }  
  
        decompresser.end();  
        return output;  
    }  	
    public static byte[] decompress(InputStream is) {  
        InflaterInputStream iis = new InflaterInputStream(is);  
        ByteArrayOutputStream o = new ByteArrayOutputStream(1024);  
        try {  
            int i = 1024;  
            byte[] buf = new byte[i];  
  
            while ((i = iis.read(buf, 0, i)) > 0) {  
                o.write(buf, 0, i);  
            }  
  
        } catch (IOException e) {  
            e.printStackTrace();  
        }  
        return o.toByteArray();  
    }  

    public static byte[] compress(byte[] data) {  
        byte[] output = new byte[0];  
  
        Deflater compresser = new Deflater();  
  
        compresser.reset();  
        compresser.setInput(data);  
        compresser.finish();  
        ByteArrayOutputStream bos = new ByteArrayOutputStream(data.length);  
        try {  
            byte[] buf = new byte[1024];  
            while (!compresser.finished()) {  
                int i = compresser.deflate(buf);  
                bos.write(buf, 0, i);  
            }  
            output = bos.toByteArray();  
        } catch (Exception e) {  
            output = data;  
            e.printStackTrace();  
        } finally {  
            try {  
                bos.close();  
            } catch (IOException e) {  
                e.printStackTrace();  
            }  
        }  
        compresser.end();  
        return output;  
    }      
	
    	
	protected synchronized static void print(String s) {
    	System.out.print(s);		
	}
	protected synchronized static void println(String s) {
    	System.out.println(s);		
	}
	protected synchronized static void p(String s) {
    	System.out.print(s);		
	}
	protected synchronized static void pln(String s) {
    	System.out.println(s);		
	}
	
	public static void sleep(long s) {
		try{
			Thread.sleep(s*1000);
		}
		catch(Exception e) {}
	}
	public static void Sleep(long s) {
		try{
			Thread.sleep(s);
		}
		catch(Exception e) {}
	}
	//
	public static void replace(byte[] data, String s, String d) {
		byte[] src = s.getBytes();
		byte[] dst = d.getBytes();
    	for(int i=0;i<data.length;i++) {
    		if(find(data, i, src)) {
    	    	for(int j=0;j<dst.length ;j++) {
    	    		data[j+i] = dst[j];
    	    	}
    	    	i += dst.length;
    		}
    	}
		
	}

	public static boolean find(byte[] data, int i, byte[] src) {
    	for(int j=0;j<src.length && i+j<data.length;j++) {
    		if(data[j+i] != src[j]) {
    			return false;
    		}
    	}
		return true;
	}
	
	public static String getPid() {
    	String pid = ManagementFactory.getRuntimeMXBean().getName().split("@")[0];  
    	return pid;
	}
	public static void exit() {
		System.exit(0);
	}
	
}
