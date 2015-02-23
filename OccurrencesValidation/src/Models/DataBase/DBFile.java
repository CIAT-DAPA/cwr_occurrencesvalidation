 /**
  * Copyright 2014 International Center for Tropical Agriculture (CIAT).
  * This file is part of:
  * Crop Wild Relatives
  * It is free software: You can redistribute it and/or modify
  * it under the terms of the GNU General Public License as published by
  * the Free Software Foundation, either version 3 of the License, or
  * at your option) any later version.
  * It is distributed in the hope that it will be useful,
  * but WITHOUT ANY WARRANTY; without even the implied warranty of
  * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  * GNU General Public License for more details.
  *
  * See <http://www.gnu.org/licenses/>.
  */

package Models.DataBase;

import Tools.FixData;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CodingErrorAction;
import java.util.ArrayList;

/**
 *
 * @author Steven Sotelo - stevenbetancurt@hotmail.com
 */
public class DBFile {
    
    /*Members class*/
    private String split;
    private String file;
    private BufferedReader reader;
    private boolean open;
    
    /*Properties*/
    /**
     * @return the split
     */
    public String getSplit() {
        return split;
    }
    
    /**
     * @param split the split to set
     */
    public void setSplit(String split) {
        this.split = split;
    }
    
    /**
     * @return the file
     */
    public String getFile() {
        return file;
    }
    
    /**
     * @param file the file to set
     */
    public void setFile(String file) {
        this.file = file;
    }
    
    /**
     * @return the reader
     */
    public BufferedReader getReader() {
        return reader;
    }
    
    /**
     * @param reader the reader to set
     */
    public void setReader(BufferedReader reader) {
        this.reader = reader;
    }
    
    /**
     * @return the open
     */
    public boolean isOpen() {
        return open;
    }
    
    /**
     * @param open the open to set
     */
    public void setOpen(boolean open) {
        this.open = open;
    }
    
    /*Methods*/
    /**
     * Method Construct
     * @param split
     * @param file
     */
    public DBFile(String split,String file)
    {
        this.split=split;
        this.file=file;
        this.open=false;
    }
    
    /**
     * Method that open file source
     */
    public void open()
    {
        try {
            CharsetDecoder decoder = Charset.forName("UTF-8").newDecoder();
            decoder.onMalformedInput(CodingErrorAction.IGNORE);
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(new File(file)), decoder));
            open = true;
        }
        catch (IOException ex) {
            System.out.println(ex);
            open=false;
        }
    }
    
    /**
     * Method that close file source
     */
    public void close()
    {
        try
        {
            reader.close();
            open=false;
        }
        catch (IOException ex)
        {
            System.out.println(ex);
            open=true;
        }
    }
    
    /**
     * Method that read line from file
     * @return Line from file, null otherwise
     */
    public String readLine()
    {
        try
        {
            return reader.readLine();
        }
        catch(IOException ex)
        {
            System.out.println(ex);
            return null;
        }
    }
    
    /**
     * Method that read line from file and split it
     * @return Split Line from file, null otherwise
     */
    public ArrayList<String> readLineSplit()
    {
        try
        {
            return FixData.lineSplit(reader.readLine(), split);
        }
        catch(IOException ex)
        {
            System.out.println(ex);
            return null;
        }
    }
    
    /**
     * Method that read line from file, split and lower it
     * @return 
     */
    public ArrayList<String> readLineSplitLower()
    {
        try
        {
            return FixData.lineSplitLower(reader.readLine(), split);
        }
        catch(IOException ex)
        {
            System.out.println(ex);
            return null;
        }
    }
}
