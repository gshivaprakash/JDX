package ad2pro.fw.utils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.apache.log4j.FileAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;

/**
 * This class helps you to create logger files
 */
public class AppLogger {
 
 public static Logger LOGGER = Logger.getLogger(AppLogger.class.getName()); 
 private static SimpleDateFormat strLogFileName = new SimpleDateFormat("MMddyy_HHmmss");
 //private static String strlogDir = new SimpleDateFormat("MMddyy").format(new Date());
 
 
  public static void createLogger(String dirPath,String appName)
  {
   try
   {
  String strModuleDir=dirPath+"//Logs";
  String logFilePath=strModuleDir+"//log_"+appName+"_"+strLogFileName.format(new Date())+".txt";
  
  FileAppender appender = new FileAppender();
     appender.setName("MyFileAppender");
     appender.setLayout( new PatternLayout("%d %-5p [%c{1}] %m %n"));
     //appender.setLayout( new PatternLayout("%d{yyyy-MM-dd HH:mm:ss} %-5p %c:%L - %m%n"));
     appender.setFile(logFilePath);
     appender.setAppend(true);
     appender.setThreshold(Level.INFO);
     //appender.setThreshold(Level.toLevel(0));
     appender.activateOptions();
     
     
     LOGGER.addAppender(appender);
  
   }
   catch(Exception e)
   {
    System.out.println("Error Occured while creating the Logger Object..::"+e.getMessage());
   }
  }
  
 
 
 public static void createDir(String dirName){
  File f = new File(dirName);
  try {
   if (!f.exists()) {
    f.mkdir();
    AppLogger.LOGGER.info("Directory Created :: " +dirName);
   }
  } catch (Throwable e) {
   AppLogger.LOGGER.error("Unable to create directory  '" +dirName+"'");
   System.out.println("Unable to create directory ");
  }
 }
 
 public static void main(String[] args) {
  
  AppLogger.createLogger("./", "JD");
  AppLogger.LOGGER.info("This is Logger file");
 }

}