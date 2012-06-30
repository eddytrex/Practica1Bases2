/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Mapeo;

import java.util.ArrayList;
import java.util.Iterator;

/**
 *
 * @author omar
 */
public class hierarchy {
    String name;
    ArrayList<String[]> fields = new ArrayList<String[]>();

    public ArrayList<String[]> getFields() {
        return fields;
    }

    public void setFields(ArrayList<String[]> fields) {
        this.fields = fields;
    }
    
    public ArrayList<String>getHeraquia()
    {
        ArrayList<String>jq=new ArrayList();
     /*   Iterator<String[]>ja=this.fields.iterator();
     /*   while(ja.hasNext())
        {
            jq.add(ja.next()[1]);
        }*/       
        int i=0;
        for(i=this.fields.size()-1;i>=0;i--)
        {
            jq.add(this.fields.get(i)[1]);
            
        }
        return jq;
    }
    
    public String getPosibleDimension()
    {
        String res="";
        if(this.fields.size()>0)
        {
        res=this.fields.get(0)[0];
        }
        return res;
    }
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}

