/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Cubo;

import Mapeo.Dimension;
import Mapeo.ForenKey;
import Mapeo.ModeloEstrella;
import Mapeo.Sql;
import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author eddytrex
 */
public class CuboE {
    
    
    public String preSelect;
    
    public ModeloTabla tm;
    public ModeloEstrella me;
    
     Dimension  X=null,Y=null,Z=null;
     
     String NivelJeraquiaX;
     String NivelJeraquiaY;
     String NivelJeraquiaZ;
    
     int NivelJX=0;
     int NivelJY=0;
     int NivelJZ=0;
     
     ForenKey Fx,Fy,Fz;   
     
     String CondJoin;
     
     String CondCol; // condición para las Columnas
     String CondECol;// Condicion para lso encabezados de las columnas
     String CondECol1;//Condicion para los encabezados de las columnas
     
     String CondRow;//Condicion para las filas  Y
     
     String CondLayer;
     
     String act;
     
     ArrayList<String>Keys=new ArrayList(); //Columnas
     
     String consultaKey;
    
     ArrayList<String>Filas=new ArrayList(); //For the slide
     
     String QuerryOfC;
     
     
     int itemsX;
     int itemsY;
     int itemsZ;
     
     
     ArrayList<Integer> iX=new ArrayList();
     ArrayList<Integer> iY=new ArrayList();
     ArrayList<Integer> iZ=new ArrayList();
     
     
     String itemsXN[];
     String itemsYN[];
     String itemsZN[];
     
     ArrayList<String[]> iXN=new ArrayList();
     ArrayList<String[]> iYN=new ArrayList();
     ArrayList<String[]> iZN=new ArrayList();
             
     
     
     String slideX;
     String sildeY;
     String slideZ;
     
     
     int indiceMetrica=0;
     
     String sMetrica="";
     
     public ArrayList<Dimension> dimX1;
     
     public CuboE(ModeloEstrella mes,ArrayList<Dimension> dminx1,int indiceM)
     {
            new Sql().ejecuta("CREATE EXTENSION tablefunc;");
         
         this.indiceMetrica=indiceM;
         
         
         this.dimX1=dminx1;
         this.me=mes;
         this.setCondicionJoin(dimX1.get(0).NombreDim,dimX1.get(1).NombreDim,dimX1.get(2).NombreDim);
         
         //this.setXDim(me.dimX1.get(0).NombreDim);
         //this.setYDim();
         //X=me.dimX1.get(0);
         //Y=me.dimX1.get(1);
         
         this.NivelJeraquiaX=X.Jerarquia.get(0);
         this.NivelJeraquiaY=Y.Jerarquia.get(0);
         this.NivelJeraquiaZ=dimX1.get(2).Jerarquia.get(NivelJZ);
         
         this.iXN.add(null);
         this.iYN.add(null);
         this.iZN.add(null);
         
         this.iX.add(0);
         this.iY.add(0);
         this.iZ.add(0);
         
         
         this.creatConidtionofKeys();
         this.setConslutaKey();
         
         this.querryDelCubo();
     }
     
     public CuboE(ModeloEstrella mes,ArrayList<Dimension> dminx1,String M)
     {
            new Sql().ejecuta("CREATE EXTENSION tablefunc;");
         
         this.sMetrica=M;
         
         
         this.dimX1=dminx1;
         this.me=mes;
         this.setCondicionJoin(dimX1.get(0).NombreDim,dimX1.get(1).NombreDim,dimX1.get(2).NombreDim);
         
         //this.setXDim(me.dimX1.get(0).NombreDim);
         //this.setYDim();
         //X=me.dimX1.get(0);
         //Y=me.dimX1.get(1);
         
         this.NivelJeraquiaX=X.Jerarquia.get(0);
         this.NivelJeraquiaY=Y.Jerarquia.get(0);
         this.NivelJeraquiaZ=dimX1.get(2).Jerarquia.get(NivelJZ);
         
         this.iXN.add(null);
         this.iYN.add(null);
         this.iZN.add(null);
         
         this.iX.add(0);
         this.iY.add(0);
         this.iZ.add(0);
         
         
         this.creatConidtionofKeys();
         this.setConslutaKey();
         
         this.querryDelCubo();
     }
     
     void querryDelCubo()
     {
         String select;
         String Querrytotal;
         if(this.sMetrica.equals("")){
         select="cast("+this.Y.NombreDim+"."+this.NivelJeraquiaY+" AS text) , "+this.X.NombreDim+"."+this.NivelJeraquiaX+",cast( sum("+this.me.metricas.get(this.indiceMetrica)+") as text)";
         }
         else
         {
         select="cast("+this.Y.NombreDim+"."+this.NivelJeraquiaY+" AS text) , "+this.X.NombreDim+"."+this.NivelJeraquiaX+",cast( sum("+this.sMetrica+") as text)";
         }
         String gby=this.NivelJeraquiaX+" , "+this.NivelJeraquiaY;
         String oby="1,2";
         String from=this.X.NombreDim+" , "+this.Y.NombreDim+" , "+this.Z.NombreDim+" , "+this.me.NombreFactTable;
         String whare="";
         if(this.CondCol!=null&&!this.CondCol.equals(""))
         {
          whare=this.CondJoin+" and ("+this.CondCol+")";
         }
         else
         { whare=this.CondJoin;}
         if(this.CondLayer!=null&&!this.CondLayer.equals(""))
         {
             whare=whare+" AND ("+this.CondLayer+")";
         }
         
         Querrytotal="SELECT "+select+" FROM "+from+" WHERE "+whare+" GROUP BY "+gby+" ORDER BY "+oby+" ";
         
         
         this.act=this.Y.NombreDim+" text ";
         Iterator<String> ncolumns=this.Keys.iterator();
         while(ncolumns.hasNext())
         {
             this.act=this.act+" , "+"\""+ncolumns.next()+"\""+" text";
         }
         
         
         this.act=" AS act ("+this.act+")";
         
         String querryReal="SELECT * FROM crosstab(\'"+Querrytotal+"\',\'"+this.preSelect+"\')"+this.act;
         
         
         if(this.itemsY!=0&&this.itemsY!=-1)
         {
             querryReal="Select * FROM ("+querryReal+") AS T FETCH FIRST  "+this.itemsY+" ROWS only;";
         }
         else if(this.itemsY==-1)
         {
             if(this.CondRow!=null&&!this.CondRow.equals(""))
             {
                 querryReal="Select * FROM ("+querryReal+") AS T"+" WHERE  "+this.CondRow+";";
             }
         }
         else
         {
             querryReal=querryReal+";";
         }
         
              this.QuerryOfC=querryReal;
             
              try {
            BufferedWriter bW= new BufferedWriter(new FileWriter("QC.txt"));
           bW.write(this.QuerryOfC+";");
           bW.close();
        } catch (IOException ex) {
            Logger.getLogger(CuboE.class.getName()).log(Level.SEVERE, null, ex);
        }
             
         int t;
         this.EjecutarQ();
         t=1+1;
     
     }
     
     void EjecutarQ()
     {
         this.tm=new ModeloTabla();
         
         
         
         ArrayList<String[]> temp=new ArrayList();
         String encabezado[]=null;
         String tempD[];
         //System.out.println(this.QuerryOfC);
         ArrayList<String[]> res=new Sql().consulta(this.QuerryOfC);
        
       
        if(res!=null)
        {
         Iterator<String[]>i=res.iterator();
         if(i.hasNext())
         {
             encabezado=i.next();
             int j=0;
             String Datos[][]=new String[res.size()-1][encabezado.length];
             while(i.hasNext())
             {          
                 Datos[j]=i.next();
                  //temp.add(i.next());
                 j++;
             }         
               tm.setDataVector(Datos,encabezado);
         }
        }
         //Datos=(String[][]) temp.toArray();
       
                  
     }
     
     public void downJeraquiaY()
     {
            if(Y.Jerarquia.size()>NivelJY+1)
           {
               NivelJY=NivelJY+1;
               this.iY.add(0);
               
               this.iYN.add(null);
               
               this.itemsY=0;
               NivelJeraquiaY=Y.Jerarquia.get(NivelJY);
           }
     }
     
     public void upJeraquiaY()
     {
            if(NivelJY>0)
           {
               NivelJY=NivelJY-1;
               
               
               
               this.itemsY=this.iY.remove(this.iY.size()-1);
               
               this.iYN.remove(this.iYN.size()-1);
               
               NivelJeraquiaY=Y.Jerarquia.get(NivelJY);
           }
     }
     
     public void downJeraquiaX()
     {
           if(X.Jerarquia.size()>NivelJX+1)
           {
               NivelJX=NivelJX+1;
               NivelJeraquiaX=X.Jerarquia.get(NivelJX);
               String tempA=this.preSelect;
               
               this.iX.add(0);
               this.iXN.add(null);
               
               this.itemsX=0;
               
               this.creatConidtionofKeys();
               this.setConslutaKey();
               
               int h;
               h=1+1;
           }
         
     }
     
     public void upJeraquiaX()
     {
           if(NivelJX>0)
           {
               NivelJX=NivelJX-1;
               NivelJeraquiaX=X.Jerarquia.get(NivelJX);
               
               this.itemsX=this.iX.remove(this.iX.size()-1);
               this.iXN.remove(this.iXN.size()-1);
               this.creatConidtionofKeys();
               this.setConslutaKey();
               
           }         
     }
     
     void setConslutaKey()
     {
         
         int j=0;
         Iterator<Integer>ix=this.iX.iterator();
         String pruebaSe="";
          String tempAS="";
          String tempHaving="";
         if(ix.hasNext())
         {
           int tempit=ix.next();
           String jeraquia=this.X.Jerarquia.get(j);
           j++;
           pruebaSe="SELECT "+jeraquia+ " FROM  ( SELECT  row_number() OVER (ORDER BY("+this.X.Jerarquia.get(0)+") )as N, "
                                        +jeraquia+" FROM "+this.X.NombreDim+" GROUP BY "
                                        +this.X.Jerarquia.get(0)+","+jeraquia+") AS T";
           if(tempit!=0&&tempit!=-1)
           {
           pruebaSe=pruebaSe+" FETCH FIRST "+tempit+" ROWS ONLY";
           }
           while(ix.hasNext())
           {
             tempit=ix.next();                
             jeraquia=this.X.Jerarquia.get(j);
             String temppruebaSe="SELECT "+jeraquia+ " FROM  ( SELECT  row_number() OVER (ORDER BY("+this.X.Jerarquia.get(0)+") )as N, "
                                        +jeraquia+" FROM "+this.X.NombreDim+
                                         " GROUP BY "
                                        +this.X.Jerarquia.get(0)+","+jeraquia+" "; 
            
             if(tempit!=0&&tempit!=-1)
             {
                 tempHaving=","+this.X.Jerarquia.get(j-1)+"  HAVING  "+this.X.Jerarquia.get(j-1)+" IN ( ";
                 
                 tempAS= "AS T"+Integer.toString(j)+" FETCH FIRST "+tempit+" ROWS ONLY";
                 
                 temppruebaSe=temppruebaSe+tempHaving+pruebaSe+" )) "+tempAS;
                 
                 pruebaSe=temppruebaSe;
             //temppruebaSe=temppruebaSe+"FETCH FIRST "+tempit+" ROWS ONLY";
             }
             else
             {
                 tempHaving=","+this.X.Jerarquia.get(j-1)+"  HAVING  "+this.X.Jerarquia.get(j-1)+" IN ( ";
                 tempAS= "AS T"+Integer.toString(j);
                 
                 temppruebaSe=temppruebaSe+tempHaving+pruebaSe+" )) "+tempAS;
                 
                 pruebaSe=temppruebaSe;
             }
             j++;
         }
         }
         
/*        preSelect = "SELECT "+this.NivelJeraquiaX+ " FROM  ( SELECT  row_number() OVER (ORDER BY("+this.NivelJeraquiaX+") )as N, "
           +this.NivelJeraquiaX+" FROM "+this.X.NombreDim+" GROUP BY "
           +this.X.Jerarquia.get(0)+","+this.NivelJeraquiaX+") AS T";
         
         
         if(this.itemsX!=0)
         {
                preSelect=preSelect+" FETCH FIRST "+this.itemsX+" ROWS ONLY;";
                //preSelect="SELECT"+this.NivelJeraquiaX+" FROM ("+preSelect+")  AS T FETCH FIRST "+this.itemsX+" ROWS ONLY;" ;
         }
         else
         {
                preSelect=preSelect+";";
         }*/
         String preSelectO;
         if(this.CondECol!=null&&!this.CondECol.equals(""))
         {
                this.preSelect=pruebaSe+" WHERE "+this.CondECol1;
                preSelectO=pruebaSe+" WHERE "+this.CondECol;
                this.CondECol="";
                this.CondECol1="";
                
                //this.CondCol="";
         }
         else
         {
         this.preSelect=pruebaSe;
         preSelectO=pruebaSe;
         }
         try {
            BufferedWriter bW= new BufferedWriter(new FileWriter("C.txt"));
           bW.write(preSelectO+";");
           bW.close();
        } catch (IOException ex) {
            Logger.getLogger(CuboE.class.getName()).log(Level.SEVERE, null, ex);
        }

          ArrayList<String[]> query = new Sql ().consulta(preSelectO+";");
          this.Keys=new ArrayList();
          Iterator <String[]> i=query.iterator();
          if(i.hasNext())
          {
              i.next();
              while(i.hasNext())
              {
                  this.Keys.add(i.next()[0]);
              }
          }
          
     
     }
     
      void creatConidtionofKeys()
      {
          
          
          
         if(this.iXN.size()!=0)
         {
             String tnitems[]=null;
                 if(this.iXN.size()==1)
                 {
                     tnitems=this.iXN.get(0);
                 }  
                 else{
                 tnitems=this.iXN.get(this.iXN.size()-1);}
                 
          if(tnitems!=null)
          {
                this.CondCol=this.X.NombreDim+"."+this.NivelJeraquiaX+"=\'\'"+tnitems[0]+"\'\'";
                this.CondECol1=this.NivelJeraquiaX+"=\'\'"+tnitems[0]+"\'\'";
                this.CondECol=this.NivelJeraquiaX+"=\'"+tnitems[0]+"\'";
                for(int i=1;i<tnitems.length;i++)
                {
                       this.CondCol=this.CondCol+" OR "+this.X.NombreDim+"."+this.NivelJeraquiaX+"= \'\'"+tnitems[i]+"\'\'";
                       
                       this.CondECol1=this.CondECol1+" OR "+this.NivelJeraquiaX+"=\'\'"+tnitems[i]+"\'\'";
                       
                       this.CondECol=this.CondECol+" OR "+this.NivelJeraquiaX+"=\'"+tnitems[i]+"\'";
                       
                       
                 }
          }
          else
          {
              this.CondCol="";
          }
         }
         
         if(this.iYN.size()!=0)
         {
             String tnitems[]=null;
             
             if(this.iYN.size()==1)
             {
                 tnitems=this.iYN.get(0);
             }
             else{
             tnitems=this.iYN.get(this.iXN.size()-1);
             }
             if(tnitems!=null)
             {
                 this.CondRow=this.Y.NombreDim+"= \'"+tnitems[0]+"\'";
                 for(int i=1;i<tnitems.length;i++)
                 {
                     this.CondRow=this.CondRow+" OR "+this.Y.NombreDim+"= \'"+tnitems[i]+"\'";
                 }
             }
         }
         
          if(this.iZN.size()!=0)
          {
              String initems[]=null;
               if(this.iZN.size()==1)
               {
                   initems=this.iZN.get(0);
               }
               else
               {
                   initems=this.iZN.get(this.iZN.size()-1);
               }
               if(initems!=null)
               {
                   this.CondLayer=this.Z.NombreDim+"."+this.NivelJeraquiaZ+"=\'\'"+initems[0]+"\'\'";
                   for(int i=1;i<initems.length;i++)
                   {
                       this.CondLayer=this.CondLayer+" OR "+this.Z.NombreDim+"."+this.NivelJeraquiaZ+"=\'\'"+initems[i]+"\'\'";
                   }
               }
          }
         /* Iterator<String> i=this.Keys.iterator();
          if(i.hasNext())
          {
              this.CondKey=this.X.NombreDim+"."+this.NivelJeraquiaX+"=\'\'"+i.next()+"\'\'";
              while(i.hasNext())
              {
                  this.CondKey=this.CondKey+" OR "+this.X.NombreDim+"."+this.NivelJeraquiaX+"=\'\'"+i.next()+"\'\'";
              }
          }*/ 
      }
      
      
     
     public void setXDim(String nombre)
     {
            Iterator<Dimension>Dim=me.dimX1.iterator();
            while(Dim.hasNext())
            {
                Dimension temp=Dim.next();
                if(temp.NombreDim.equals(nombre)){X=temp; }
            }           
            Iterator<ForenKey>fk=me.FK.iterator();
            while(fk.hasNext())
            {
                ForenKey temp=fk.next();
                if(temp.tablaPadre.equals(nombre)){Fx=temp; return;}
            }           
     }
     
     public void setYDim(String nombre)
     {
            Iterator<Dimension>Dim=me.dimX1.iterator();
            while(Dim.hasNext())
            {
                Dimension temp=Dim.next();
                if(temp.NombreDim.equals(nombre)){Y=temp; }
            }           
            Iterator<ForenKey>fk=me.FK.iterator();
            while(fk.hasNext())
            {
                ForenKey temp=fk.next();
                if(temp.tablaPadre.equals(nombre)){Fy=temp; return;}
            }           
     }
     
     public void setZDim(String nombre)
     {
            Iterator<Dimension>Dim=me.dimX1.iterator();
            while(Dim.hasNext())
            {
                Dimension temp=Dim.next();
                if(temp.NombreDim.equals(nombre)){Z=temp; }
            }          
            Iterator<ForenKey>fk=me.FK.iterator();
            while(fk.hasNext())
            {
                ForenKey temp=fk.next();
                if(temp.tablaPadre.equals(nombre)){Fz=temp; return;}
            }           
     }
     
     public void setCondicionJoin(String x,String y,String z)
     {  String parteJx;
         String parteJy;
         String parteJz;
         if(!x.equals(y))
         { 
            this.setXDim(x);
            this.setYDim(y);                                  
            this.setZDim(z);
               parteJx=Fx.GetSubCondicion();
               parteJy=Fy.GetSubCondicion();
               parteJz=Fz.GetSubCondicion();
               
               this.CondJoin=parteJx+" and "+parteJy+" and "+parteJz;
                                  
         }
     }
     
     
     
     public void DirllDownX()
     {
         this.downJeraquiaX();
         
        // this.setConslutaKey();
        //this.creatConidtionofKeys();
         this.querryDelCubo();     
     }
     
     public void DrillUpX()
     {
          this.upJeraquiaX();
         
         //this.setConslutaKey();
        // this.creatConidtionofKeys();
         this.querryDelCubo();     
     }
     
     
     public void DrillDownY()
     {
         this.downJeraquiaY();
         
    //    this.setConslutaKey();
//         this.creatConidtionofKeys();
         this.querryDelCubo();  
     }
     
     public void DirllUpY()
     {
         this.upJeraquiaY();
         
         //this.setConslutaKey();  
         //this.creatConidtionofKeys();
         this.querryDelCubo();     
     }
     
     
     
     
     public void Dice(String eje)
     {
         
         if(eje.equals("i"))
         {
               this.setCondicionJoin(this.Y.NombreDim,this.X.NombreDim,this.Z.NombreDim);
               
               ArrayList<String[]> tempIN=new ArrayList();
               
               tempIN=this.iXN;
               this.iXN=this.iYN;
               this.iYN=tempIN;
               
               ArrayList<Integer>tempIs;
               
               tempIs=this.iX;
               this.iX=this.iY;
               this.iY=tempIs;
               
               
               int tempI=this.itemsX;
               this.itemsX=this.itemsY;
               this.itemsY=tempI;
               
               int tempNJ=this.NivelJX;               
               this.NivelJX=this.NivelJY;
               this.NivelJY=tempNJ;
               
               String tempNivelJeraquia=this.NivelJeraquiaX;               
               this.NivelJeraquiaX=this.NivelJeraquiaY;               
               this.NivelJeraquiaY=tempNivelJeraquia;
               
               String tempSlide=this.slideX;
               this.slideX=this.sildeY;  
               this.sildeY=tempSlide;              
               
               
               this.creatConidtionofKeys();
               this.setConslutaKey();
               
               this.querryDelCubo();
               
               Dimension tempD=me.dimX1.get(0);               
               me.dimX1.set(0,me.dimX1.get(1));
               me.dimX1.set(1, tempD);              
         }
         
         if(eje.equals("j"))
         {
             this.setCondicionJoin(this.Z.NombreDim,this.Y.NombreDim,this.X.NombreDim);
             
             ArrayList<String[]> tempIN=new ArrayList();
             
             tempIN=this.iXN;
             this.iXN=this.iZN;
             this.iZN=tempIN;
             
             ArrayList<Integer>tempIs;
             
             tempIs=this.iX;
             this.iX=this.iZ;
             this.iZ=tempIs;
             
             int tempI=this.itemsX;
             this.itemsX=this.itemsZ;
             this.itemsZ=tempI;
             this.itemsY=this.itemsY;
             
             
             int tempNJ=this.NivelJX;
             this.NivelJX=this.NivelJZ;
             this.NivelJY=this.NivelJY;
             this.NivelJZ=this.NivelJX;
             
             
             
             String tempNivelJeraquia=this.NivelJeraquiaX;
             this.NivelJeraquiaX=this.NivelJeraquiaZ;
             this.NivelJeraquiaZ=tempNivelJeraquia;
             
             //this.NivelJeraquiaX=dimX1.get(2).Jerarquia.get(this.NivelJX);
             //this.NivelJeraquiaY=this.NivelJeraquiaY;
             
             String tempSlide=this.slideX;
             this.slideX=this.slideZ;
             this.sildeY=this.sildeY;
             this.slideZ=tempSlide;
             
               this.creatConidtionofKeys();
               this.setConslutaKey();
               
               this.querryDelCubo();
               
               Dimension tempD=this.dimX1.get(0);
               this.dimX1.set(0, this.dimX1.get(2));
               this.dimX1.set(2, tempD);
         
         }
         if(eje.equals("k"))
         {
             this.setCondicionJoin(this.X.NombreDim,this.Z.NombreDim ,this.Y.NombreDim);
             
             ArrayList<String[]> tempIN=new ArrayList();
             
             tempIN=this.iYN;
             this.iYN=this.iZN;
             this.iZN=tempIN;
             
             
             ArrayList<Integer>tempIs;
             
             tempIs=this.iY;
             this.iY=this.iZ;
             this.iZ=tempIs;
             
             int tempI=this.itemsY;
             this.itemsY=this.itemsZ;
             this.itemsZ=tempI;
             this.itemsX=this.itemsX;
                     
             
             
             int tempNJ=this.NivelJY;
             this.NivelJY=this.NivelJZ;
             this.NivelJX=this.NivelJX;
             this.NivelJZ=tempNJ;
             
             String tempNivelJeraquia=this.NivelJeraquiaY;
             this.NivelJeraquiaY=this.NivelJeraquiaZ;
             this.NivelJeraquiaZ=tempNivelJeraquia;
             
             String tempSlide=this.sildeY;
             this.sildeY=this.slideZ;
             this.slideX=this.slideX;
             this.slideZ=tempSlide;
             
             this.creatConidtionofKeys();
             this.setConslutaKey();
             
             this.querryDelCubo();
             
             Dimension tempD=this.dimX1.get(1);
             this.dimX1.set(1, this.dimX1.get(2));
             this.dimX1.set(2, tempD);
          }         
     }
     
     public void Slide(String D,int items)
     {
         if(D.equals("x"))
         {
                if(this.iXN.size()>0)
                {
                this.iXN.set(this.iXN.size()-1,null);
                }
                this.itemsX=items;
                this.iX.set(this.iX.size()-1, items);
                
                this.creatConidtionofKeys();
                this.setConslutaKey();
                
                this.querryDelCubo();
         }
         if(D.equals("y"))
         {
                this.itemsY=items;
                
                this.iY.set(this.iY.size()-1, items);
                this.setConslutaKey();
                this.creatConidtionofKeys();
                this.querryDelCubo();
         }
         if(D.equals("z"))
         {
                this.itemsZ=items;
                this.iZ.set(this.iZ.size()-1, items);
                
                this.creatConidtionofKeys();
                this.setConslutaKey();
                
                this.querryDelCubo();
         }  
     }
     
     
    public void Slide(String D, String items[])
     {
         if(D.equals("x"))
         {
                this.iXN.set(this.iXN.size()-1, items);
                
                this.itemsX=-1;
                this.iX.set(this.iX.size()-1, this.itemsX);
                
                this.creatConidtionofKeys();
                this.setConslutaKey();
                
                this.querryDelCubo();
         }
         if(D.equals("y"))
         {
                this.iYN.set(this.iYN.size()-1, items);
                
                this.itemsY=-1;
                this.iY.set(this.iY.size()-1, this.itemsY);
                
                this.creatConidtionofKeys();
                this.setConslutaKey();
                
                this.querryDelCubo();
         }
         if(D.equals("z"))
         {
                this.iZN.set(this.iZN.size()-1, items);
                
                this.itemsZ=-1;
                this.iY.set(this.iZ.size()-1, this.itemsZ);
                
                this.creatConidtionofKeys();
                this.setConslutaKey();
                
                this.querryDelCubo();
         }  
     }
}
