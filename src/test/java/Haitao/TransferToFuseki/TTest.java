package Haitao.TransferToFuseki;

import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;

import Haitao.TransferToFuseki.TTest.Localv;

//import testpac.TestBean;


public class TTest {
	private static String aa = "11";
   public static void main(String[] args) throws IOException, ClassNotFoundException, InstantiationException, IllegalAccessException{
////	   System.out.println("aaaaa");
//	   Enumeration<URL> dir = Thread.currentThread().getContextClassLoader().getResources("co/drive/client/template/file.add.json");
//	   while(dir.hasMoreElements()){
//		  URL url = dir.nextElement();
//		  System.out.println(url);
//	   }
//	   URL furl = TTest.class.getResource("/co/drive/client/template/file.add.json");
//	   System.out.println(furl);
////	  TTest.class.getClassLoader().getResources("file.add.json");
////	   TestBean bean1 = new TestBean();
////	   System.out.println(bean1.getClass().getClassLoader().toString());
//	   URL url = new URL("file:/D:/test.jar");
//	   URL[] urls = new URL[1];
//	   urls[0] = url;
//	   MyClassLoader loader = new MyClassLoader(urls, TTest.class.getClassLoader());
//	   Class<?> classz = loader.loadClass("Haitao.ClassA");
//	   System.out.println(classz.getClassLoader().toString());
//	   Object oo = classz.newInstance();
//	   
//	   Class<?> classzz = loader.loadClass("Haitao.TransferToFuseki.ClassA");
//	   System.out.println(classzz.getClassLoader().toString());
//	   Object ooo = classzz.newInstance();
	   Thread t1 = new Thread(trun);
	   Thread t2 = new Thread(trun);
	   Thread t3 = new Thread(trun);
	   
	   t1.start();
	   t2.start();
	   t3.start();
	   
   }
   
   private static Runnable trun = new Runnable(){
	public void run() {
		Localv vv = tlv.get();
		String tname = Thread.currentThread().getName();
		int vvId = vv.hashCode();
		vv.name = tname;
		System.out.println(tname + "-" + vvId + "-" + vv.name);
		
	}
	   
   };
   
   private static ThreadLocal<Localv> tlv = new ThreadLocal<Localv>(){
		    protected Localv initialValue() {
		        return new Localv();
		    }
   };
   
   public static class Localv {
	   private String name;
	   private Localv(){
		   this.name = "default";
	   };
	   public Localv(String name){
		   this.name = name;
	   }
   }
}
