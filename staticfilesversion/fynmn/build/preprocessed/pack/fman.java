/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pack;
import javax.microedition.lcdui.*; 
import javax.microedition.midlet.*;
import javax.microedition.rms.RecordEnumeration;
import javax.microedition.rms.RecordStore;
import javax.microedition.rms.RecordStoreException;
import java.io.*;
import javax.microedition.rms.RecordStoreNotOpenException;

/**
 * @author lalit som
 */
public class fman extends MIDlet implements CommandListener {
private StringItem si;
private Form readform = new Form("Lalit");
private Form menuform;
private String finals ="";
 private RecordStore rs = null;
static final String REC_STORE = "ReadWriteRMS";
static final int REC_ID =1;
public String error ="-->  ";
public String bookname ="feynman";
Display display;
public int startIndex =51;
Command f1;
List li;


public fman(){
    display = Display.getDisplay(this);
    f1 = new Command("f1",Command.EXIT,1);
    openRecStore();
    li = new List(readRecord(REC_ID)+ getfiledata("conf.txt"),List.IMPLICIT);        
    for(int i =startIndex;i<=startIndex+50;i++){
    li.append( i + "" ,null);
    li.addCommand(f1);
    li.setCommandListener(this);        
    }    
    menuform = new Form("menu");    
    
}


private String getfiledata(String fname){
  InputStream is = getClass().getResourceAsStream(fname);
  
  StringBuffer sb = new StringBuffer();
  try{
  int chars, i = 0;
  while ((chars = is.read()) != -1){
  sb.append((char) chars);
  }
  return sb.toString();
  }catch (Exception e){}
  return fname;
  }





private void showf(String name){
      
    openRecStore();            
    writeRecord(name,REC_ID);    
    closeRecStore();   
    li.setTitle(name + bookname);
    readform.deleteAll();
    if(name.length() ==1){
        name = "00" + name;
    }
    if(name.length() ==2){
        name = "0" + name;
    }
      String data = getfiledata("feynman_"+name+"(133).txt");
        si = new StringItem(error +  data,"END");
        readform.append(si);    
        display.setCurrent(readform);
        
  }




    public void startApp() {                
        display.setCurrent(li);        
    }
    
    public void commandAction(Command c, Displayable d){
        
        List selected = (List)display.getCurrent();
        
        showf( "" + (selected.getSelectedIndex() + startIndex) );
        
    }
    
    
    
    public void pauseApp() {
    }
    
    public void destroyApp(boolean unconditional) {
    }
    
    
    
    //record store
    
    
      public void openRecStore(){
    try{
      rs = RecordStore.openRecordStore(REC_STORE, true );
      if(rs.getNumRecords() == 0){
           byte[] rec = "new ".getBytes();
          rs.addRecord(rec, 0, rec.length);
      }
    }catch (Exception e){}
  }    
      
  public void closeRecStore(){
    try{
      rs.closeRecordStore();
    }catch (Exception e){}
  }

    public void writeRecord(String str, int id){
    byte[] rec = str.getBytes();
    try{
        rs.setRecord(id,rec, 0, rec.length);           
    }catch (Exception e){
            try {
                error = "writerecord" +  rs.getNumRecords() +  e.toString();
            } catch (RecordStoreNotOpenException ex) {
                error = "writerecord" +  2 + ex.toString();
            }
    }
  }

  public String readRecord(int id){
      String allRecords ="0";
    try{
      byte[] recData = new byte[rs.getRecordSize(id)];
      int len;
        len = rs.getRecord(id, recData, 0);        
        allRecords += (new String(recData, 0, len)) +":";              
    }catch (Exception e){
    error = "readrecord" + e.toString();
    }    
    return allRecords;
  }
    
    
    
}
