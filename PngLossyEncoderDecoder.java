package assign_4;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.BitSet;

import javax.imageio.ImageIO;

import com.sun.javafx.image.PixelAccessor;

public class PngLossyEncoderDecoder {
public PngLossyEncoderDecoder() {
	// TODO Auto-generated constructor stub
}
public void Compress(String src) throws IOException{
	double minEntropy = 0,Entropyprev=0,Entropytop=0,Entropyave=0,EntropyaveLossy=0,Entropymid=0,EntropymidLossy=0,Entropydefult=0,EntropyprevLossy=0,EntropytopLossy=0;
	BufferedImage imgBuffer = ImageIO.read(new File(src));
	byte[][]pxtop = new byte[2][imgBuffer.getWidth()*3];
	byte[] pxprev = new byte[imgBuffer.getWidth()*3];
	byte[] pxprevnew = new byte[imgBuffer.getWidth()*3];
	byte[] pxtopnew = new byte[imgBuffer.getWidth()*3];
	byte[] pxavenew = new byte[imgBuffer.getWidth()*3];
	byte[] pxmidnew= new byte[imgBuffer.getWidth()*3];
	byte[] pxprevnewL = new byte[imgBuffer.getWidth()*3];
	byte[] pxtopnewL = new byte[imgBuffer.getWidth()*3];
	byte[] pxavenewL= new byte[imgBuffer.getWidth()*3];
	byte[] pxmidnewL= new byte[imgBuffer.getWidth()*3];
	int [] colorpx=new int[256];
	File file=new File(src);
	 InputStream input = new FileInputStream(file);
	 byte[] bytes=new byte[(int) file.length()];
	 input.read(bytes, 0,bytes.length);
	 Entropytop=PngTop(src, pxtop,pxtopnew);
	 byte[] pixels=new byte[imgBuffer.getWidth()*imgBuffer.getHeight()*3];
     for(int i=0;i<pixels.length;i++){
     	pixels[i]=bytes[i+54];
     }
  /* for(int i=0;i<imgBuffer.getHeight();i++){
    	 for(int j=0;j<imgBuffer.getWidth()*3;j++){
    		 System.out.print(pixels[i*imgBuffer.getWidth()*3+j]+"\t");
    	 }
    	 System.out.println();
     }*/
	File file2=new File(src.substring(0, src.length()-3)+"LIM-prev");
    OutputStream out=new FileOutputStream(file2);
    byte choose=0;
    byte[] beforepx=new byte[54];
    for(int i=0;i<54;i++){
    	beforepx[i]=bytes[i];
    }
    out.write(beforepx);
	for(int i=0;i<imgBuffer.getHeight();i++){
		if(i==0){
		for(int j=0;j<imgBuffer.getWidth()*3;j++){
			pxprev[j]=pixels[i*imgBuffer.getWidth()*3+j];
			
		}
		Entropyprev=PngPrev(src, pxprev,pxprevnew);
		choose=1;
		out.write(choose);
		out.write(pxprevnew);
		for(int j=0;j<imgBuffer.getWidth()*3;j++){
			pxtop[1][j]=pixels[(i)*imgBuffer.getWidth()*3+j];
		}
		}
		else{
			for(int j=0;j<imgBuffer.getWidth()*3;j++){
				pxprev[j]=pixels[i*imgBuffer.getWidth()*3+j];
				if(pxprev[j]<0)
			   		colorpx[pxprev[j]+256]++;
			   	else
			   		colorpx[pxprev[j]]++;
			}
			for(int j=0;j<256;j++){
				if(colorpx[j]!=0){
					minEntropy+=((double)colorpx[j]/pxprev.length)*((Math.log((double)colorpx[j]/pxprev.length) / Math.log(2)))*-1;
				}
			}
			for(int j=0;j<imgBuffer.getWidth()*3;j++){
				pxtop[0][j]=pixels[(i)*imgBuffer.getWidth()*3+j];
			}
			choose=0;
			minEntropy=10000000;
			Entropyprev=PngPrev(src, pxprev,pxprevnew);
			if(Entropyprev<minEntropy){
				choose=1;
				minEntropy=Entropyprev;
			}
			Entropytop=PngTop(src, pxtop,pxtopnew);	
			if(Entropytop<minEntropy){
				choose=2;
				minEntropy=Entropytop;
			}
			Entropyave=PngAve(src,pxtop,pxavenew);
			if(Entropyave<minEntropy){
				choose=3;
				minEntropy=Entropyave;
			}
			Entropymid=PngMid(src,pxtop,pxmidnew);
			if(Entropymid<minEntropy){
				choose=4;
				minEntropy=Entropymid;
			}
		    EntropyprevLossy=PngPrevLossy(src, pxprev, pxprevnewL);
			if(EntropyprevLossy<minEntropy){
				choose=5;
				minEntropy=EntropyprevLossy;
			}
			EntropytopLossy=PngTopLossy(src, pxtop,pxtopnewL);
			if(EntropytopLossy<minEntropy){
				choose=6;
				minEntropy=EntropytopLossy;
			}
			EntropyaveLossy=PngAveLossy(src,pxtop,pxavenewL);
		if(EntropyaveLossy<minEntropy){
				choose=7;
				minEntropy=EntropyaveLossy;
			}
		EntropymidLossy=PngMidLossy(src, pxtop,pxmidnewL);
		if(EntropymidLossy<minEntropy){
			choose=8;
			minEntropy=EntropymidLossy;
		}
			out.write(choose);
			if(choose==0){
				out.write(pxprev);
			}
			else if(choose==1){
			out.write(pxprevnew);
			}
			else if(choose==2){
				out.write(pxtopnew);
			}
			else if(choose==3){
				out.write(pxavenew);
			}
			else if(choose==4){
				out.write(pxmidnew);
			}
			else if (choose==5){
				out.write(pxprevnewL);
			}
			else if(choose==6){
				out.write(pxtopnewL);
			}
			else if(choose==7){
				out.write(pxavenewL);
			}
			else{
				out.write(pxmidnewL);
			}
			if(choose>=0&&choose<5){
				for(int j=0;j<imgBuffer.getWidth()*3;j++){
					pxtop[1][j]=pixels[(i)*imgBuffer.getWidth()*3+j];
				}
			}
			else if(choose==5) {
				for(int j=0;j<imgBuffer.getWidth()*3;j++){
					if(j<3)
						pxtop[1][j]=pixels[(i)*imgBuffer.getWidth()*3+j];
			    	else{
			    		pxtop[1][j]=(byte) ((pxtop[1][j-3]&0xFF)+(pxprevnewL[j]&0xFF));
			    	}
				}
			}
			else if(choose==6) {
				for(int j=0;j<imgBuffer.getWidth()*3;j++){
					if(j<3)
						pxtop[1][j]=pixels[(i)*imgBuffer.getWidth()*3+j];
			    	else{
			    		pxtop[1][j]=(byte) ((pxtop[1][j]&0xFF)+(pxtopnewL[j]&0xFF));
			    	}
				}
			}
			else if(choose==7){
				for(int j=0;j<imgBuffer.getWidth()*3;j++){
					if(j<3)
						pxtop[1][j]=pixels[(i)*imgBuffer.getWidth()*3+j];
			    	else{
			    		pxtop[1][j]=(byte) (((pxtop[1][j]&0xFF)+(pxtop[1][j-3]&0xFF))/2+(pxavenewL[j]&0xFF));
			    	}
					//System.out.println((byte) (((int)pxtop[1][j]+(int)pxtop[1][j-3])/2+(int)pxavenewL[j]));
				}
			}
			else if(choose==8){
				byte[] temppx=new byte[pxtop[1].length];
				for(int k=0;k<temppx.length;k++)
					temppx[k]=pxtop[1][k];
				for(int j=0;j<imgBuffer.getWidth()*3;j++){
					if(j<3)
						pxtop[1][j]=pixels[(i)*imgBuffer.getWidth()*3+j];
			    	else{
			    		pxtop[1][j]=(byte) (((pxtop[1][j]&0xFF)+(pxtop[1][j-3]&0xFF)-(temppx[j-3]&0xFF))+(pxmidnewL[j]&0xFF));
			    	}
				}
			}
			
			}	
		
		//minEntropy=0;
		for(int j=0;j<256;j++){
			colorpx[j]=0;
		}
	}
	byte[] afterpx=new byte[bytes.length-54-imgBuffer.getWidth()*imgBuffer.getHeight()*3];
	for(int i=imgBuffer.getWidth()*imgBuffer.getHeight()*3+54;i<bytes.length;i++){
		afterpx[i-(imgBuffer.getWidth()*imgBuffer.getHeight()*3+54)]=bytes[i];
	}
	out.write(afterpx);
	HufmannEncoderDecoder x=new HufmannEncoderDecoder();
	String[] s=new String[2];
	s[0]=src.substring(0, src.length()-3)+"LIM-prev";
	String[] g=new String[2];
	g[0]=src.substring(0, src.length()-3)+"LIM";
	x.CompressWithArray(s,g);
	out.close();
	file2.delete();
}
public double PngMidLossy(String src,byte[][] bytes,byte [] newpx) throws IOException{
	int[] colorpx=new int[256];
	 double Entropy=0;
	 byte x=0;
	 int count=0;
	 for(int j=0;j<3;j++){
	for(int i=0;i<newpx.length;i=i+3){
		if(i<3){
			newpx[i+j]=bytes[0][i+j];
			x=bytes[0][i+j];
		}else{
			if(((bytes[0][i+j]&0xFF)-((bytes[1][i+j]&0xFF)+(x&0xFF)-(bytes[1][i+j-3]&0xFF)))<100&&((bytes[0][i+j]&0xFF)-((bytes[1][i+j]&0xFF)+(x&0xFF)-(bytes[1][i+j-3]&0xFF)))>-100){
		    newpx[i+j]=(byte)((bytes[0][i+j]&0xFF)-((bytes[1][i+j]&0xFF)+(x&0xFF)-(bytes[1][i+j-3]&0xFF)));
		    x=bytes[0][i+j];
			}
			else if(((bytes[0][i+j]&0xFF)-((bytes[1][i+j]&0xFF)+(x&0xFF)-(bytes[1][i+j-3]&0xFF)))<100){
				newpx[i+j]=(byte)-100;
				x=(byte)((bytes[1][i+j]&0xFF)+(x&0xFF)-(bytes[1][i+j-3]&0xFF)-(100));
				count++;
			}else{
				newpx[i+j]=(byte)+100;
				x=(byte)(((bytes[1][i+j]&0xFF)+(x&0xFF)-(bytes[1][i+j-3]&0xFF))+(100));
				count++;
			}
		}
		if(newpx[i]<0)
   		colorpx[newpx[i]+256]++;
   	else
   		colorpx[newpx[i]]++;
	}
	 }
	for(int i=0;i<256;i++){
		if(colorpx[i]!=0){
			Entropy+=((double)colorpx[i]/bytes[0].length)*((Math.log((double)colorpx[i]/bytes[0].length) / Math.log(2)))*-1;
		}
	}
	if(count>newpx.length*0.1)
		Entropy=10000;
	return Entropy;
	
}

public double PngAveLossy(String src,byte[][] bytes,byte [] newpx) throws IOException{
	int[] colorpx=new int[256];
	 double Entropy=0;
	 byte x=0;
	 int count=0;
	 for(int j=0;j<3;j++){
	for(int i=0;i<newpx.length;i=i+3){
		if(i<3){
			newpx[i+j]=bytes[0][i+j];
			x=bytes[0][i+j];
		}else{
			if(((bytes[0][i+j]&0xFF)-((bytes[1][i+j]&0xFF)+(x&0xFF))/2)<100&&((bytes[0][i+j]&0xFF-(bytes[1][i+j]&0xFF+(x&0xFF))/2)>-100)){
				newpx[i+j]=(byte)((bytes[0][i+j]&0xFF)-(((bytes[1][i+j]&0xFF)+(x&0xFF))/2));
				x=bytes[0][i+j];
			}
			else if(((bytes[0][i+j]&0xFF)-((bytes[1][i+j]&0xFF+(x&0xFF))/2))<100){
				newpx[i+j]=(byte)-100;
				x=(byte)(((x&0xFF)+(bytes[1][i+j]&0xFF))/2-(100));
				count++;
			}else{
				newpx[i+j]=(byte)+100;
				x=(byte)(((x&0xFF)+(bytes[1][i+j]&0xFF))/2+(100));
				count++;
			}	
		}
		if(newpx[i+j]<0)
   		colorpx[newpx[i+j]+256]++;
   	else
   		colorpx[newpx[i+j]]++;

	}
	 }
	for(int i=0;i<256;i++){
		if(colorpx[i]!=0){
			Entropy+=((double)colorpx[i]/bytes[0].length)*((Math.log((double)colorpx[i]/bytes[0].length) / Math.log(2)))*-1;
		}
	}
	if(count>newpx.length*0.1)
		Entropy=10000;
	return Entropy;
	
}
public double PngTopLossy(String src,byte[][] bytes,byte [] newpx) throws IOException{
	
	int[] colorpx=new int[256];
	 double Entropy=0;
	 int count=0;
	for(int i=0;i<newpx.length;i++){
		if(i<3){
			newpx[i]=bytes[0][i];
		}
		else{
			if(((bytes[0][i]&0xFF)-(bytes[1][i]&0xFF))<100&&((bytes[0][i]&0xFF)-(bytes[1][i]&0xFF)>-100)){
				newpx[i]=(byte)((bytes[0][i]&0xFF)-(bytes[1][i]&0xFF));
			}
			else if(((bytes[0][i]&0xFF)-(bytes[1][i]&0xFF))<100){
				newpx[i]=(byte)-100;
				count++;
			}else{
				newpx[i]=(byte)100;
				count++;
			}
		}	
		if(newpx[i]<0)
   		colorpx[newpx[i]+256]++;
   	else
   		colorpx[newpx[i]]++;
	}
	for(int i=0;i<256;i++){
		if(colorpx[i]!=0){
			Entropy+=((double)colorpx[i]/bytes[0].length)*((Math.log((double)colorpx[i]/bytes[0].length) / Math.log(2)))*-1;
		}
	}
	if(count>newpx.length*0.1)
		Entropy=10000;
	return Entropy;
	
}
public double PngPrevLossy(String src,byte[] bytes,byte[] newpx) throws IOException{
	 //byte[] newpx=new byte[bytes.length];
	 int[] colorpx=new int[256];
	 double Entropy=0;
	 byte x=0;
	 int count=0;
	 for(int j=0;j<3;j++){
	    for(int i=0;i<bytes.length;i=i+3){
				if(i<3){
					newpx[i+j]=bytes[i+j];
					x=bytes[i+j];
				}
				else{
					if(((bytes[i+j]&0xFF)-(x&0xFF))<100&&((bytes[i+j]&0xFF)-(x&0xFF)>-100)){
						newpx[i+j]=(byte)((bytes[i+j]&0xFF)-(x&0xFF));
						x=(byte)((bytes[i+j]&0xFF));
					}
					else if((bytes[i+j]&0xFF)-(x&0xFF)<100){
						newpx[i+j]=(byte)-100;
						x=(byte)((x&0xFF)-100);
						count++;
					}else{
						newpx[i+j]=(byte)100;
						x=(byte)((x&0xFF)+100);
						count++;
					}
				}
				if(newpx[i+j]<0)
			   		colorpx[newpx[i+j]+256]++;
			   	else
			   		colorpx[newpx[i+j]]++;
				}
			}
		
	for(int i=0;i<256;i++){
		if(colorpx[i]!=0){
			Entropy+=((double)colorpx[i]/bytes.length)*((Math.log((double)colorpx[i]/bytes.length) / Math.log(2)))*-1;
		}
	}
	if(count>newpx.length*0.1)
		Entropy=1000;
	return Entropy;
}
public double PngMid(String src,byte[][] bytes,byte [] newpx) throws IOException{
	int[] colorpx=new int[256];
	 double Entropy=0;
	for(int i=0;i<newpx.length;i++){
		if(i<3){
			newpx[i]=bytes[0][i];
		}else{
		newpx[i]=(byte)((bytes[0][i]&0xFF)-((bytes[1][i]&0xFF)+(bytes[0][i-3]&0xFF)-(bytes[1][i-3]&0xFF)));}
		if(newpx[i]<0)
   		colorpx[newpx[i]+256]++;
   	else
   		colorpx[newpx[i]]++;
	}
	for(int i=0;i<256;i++){
		if(colorpx[i]!=0){
			Entropy+=((double)colorpx[i]/bytes[0].length)*((Math.log((double)colorpx[i]/bytes[0].length) / Math.log(2)))*-1;
		}
	}
	
	return Entropy;
	
}
public double PngTop(String src,byte[][] bytes,byte [] newpx) throws IOException{
	
	int[] colorpx=new int[256];
	 double Entropy=0;
	for(int i=0;i<newpx.length;i++){
		if(i<3){
			newpx[i]=bytes[0][i];
		}else{
		newpx[i]=(byte)((bytes[0][i]&0xFF)-(bytes[1][i]&0xFF));}
		if(newpx[i]<0)
   		colorpx[newpx[i]+256]++;
   	else
   		colorpx[newpx[i]]++;
	}
	for(int i=0;i<256;i++){
		if(colorpx[i]!=0){
			Entropy+=((double)colorpx[i]/bytes[0].length)*((Math.log((double)colorpx[i]/bytes[0].length) / Math.log(2)))*-1;
		}
	}
	
	return Entropy;
	
}
public int ByteToInt(byte b){ //change the binary numbers to only positive binary numbers
	int ret=0;
	BitSet set = BitSet.valueOf(new byte[] { b });
	for(int i=0;i<8;i++){
		if(set.get(i))
		ret=(int) (ret+Math.pow(2, i));
	}
	return ret;
}

public double PngPrev(String src,byte[] bytes,byte[] newpx) throws IOException{
	 //byte[] newpx=new byte[bytes.length];
	 int[] colorpx=new int[256];
	 double Entropy=0;
	for(int i=0;i<newpx.length;i++){
		if(i<3){
			newpx[i]=bytes[i];
		}else{
		newpx[i]=(byte)((bytes[i]&0xFF)-(bytes[i-3]&0xFF));}
		if(newpx[i]<0)
    		colorpx[newpx[i]+256]++;
    	else
    		colorpx[newpx[i]]++;
	}
	for(int i=0;i<256;i++){
		if(colorpx[i]!=0){
			Entropy+=((double)colorpx[i]/bytes.length)*((Math.log((double)colorpx[i]/bytes.length) / Math.log(2)))*-1;
		}
	}
	return Entropy;
}
public double PngAve(String src,byte[][] bytes,byte [] newpx) throws IOException{
	
	int[] colorpx=new int[256];
	 double Entropy=0;
	for(int i=0;i<newpx.length;i++){
		if(i<3){
			newpx[i]=bytes[0][i];
		}else{
		newpx[i]=(byte)((bytes[0][i]&0xFF)-((bytes[1][i]&0xFF)+(bytes[0][i-3]&0xFF))/2);}
		if(newpx[i]<0)
   		colorpx[newpx[i]+256]++;
   	else
   		colorpx[newpx[i]]++;
	}
	for(int i=0;i<256;i++){
		if(colorpx[i]!=0){
			Entropy+=((double)colorpx[i]/bytes[0].length)*((Math.log((double)colorpx[i]/bytes[0].length) / Math.log(2)))*-1;
		}
	}
	
	return Entropy;
	
}

public void Decompress(String src) throws IOException{
	System.out.println();
	HufmannEncoderDecoder x=new HufmannEncoderDecoder();
	String [] s=new String[2];
	s[0]=src;
	String [] g=new String[2];
	g[0]=src.substring(0, src.length()-3)+"LIMM-prev";
	x.Decompress(s,g);
	BufferedImage imgBuffer = ImageIO.read(new File(src.substring(0, src.length()-3)+"LIMM-prev"));
	File file=new File(src.substring(0, src.length()-3)+"LIMM-prev");
	 InputStream input = new FileInputStream(file);
	 byte[] bytes=new byte[(int) file.length()];
	 input.read(bytes, 0,bytes.length);
	 byte[][] pix=new byte[imgBuffer.getHeight()][imgBuffer.getWidth()*3];
	 byte[] beforpx=new byte[54];
	 input.close();
	 file.delete();
	 File file2=new File(src.substring(0, src.length()-4)+"-LIM.bmp");
	    OutputStream out=new FileOutputStream(file2);
	 for(int i=0;i<54;i++){
		 beforpx[i]=bytes[i];
	 }
	 out.write(beforpx);
	 byte[] choose=new byte[imgBuffer.getHeight()];
	 for(int i=0;i<imgBuffer.getHeight();i++){
		 choose[i]=bytes[i*imgBuffer.getWidth()*3+54+i];
		 for(int j=0;j<imgBuffer.getWidth()*3;j++){
			 pix[i][j]=bytes[i*imgBuffer.getWidth()*3+j+i+54+1];
		 }
	 }
	   /* for(int i=0;i<imgBuffer.getHeight();i++){
		 for(int j=0;j<imgBuffer.getWidth()*3;j++){
			 System.out.print(pix[i][j]+"\t ");
		 }
		 System.out.println();

		 }*/
	 System.out.println();
	 
	 byte[][] realpix=new byte[imgBuffer.getHeight()][imgBuffer.getWidth()*3];
	 boolean f=true;
	 byte ff=0;
	 for(int i=0;i<pix.length;i++){
		 for(int j=0;j<pix[0].length;j++){
			 if(j<3){
				 realpix[i][j]=pix[i][j];
			 }
			 else{
			  if(choose[i]==0){
			    realpix[i][j]=(byte)(pix[i][j-3]&0xFF);
			 }
			 else if(choose[i]==1||choose[i]==5){
				 realpix[i][j]=(byte)((realpix[i][j-3]&0xFF)+(pix[i][j]&0xFF));
			 }
			 else if(choose[i]==2||choose[i]==6){
				 realpix[i][j]=(byte)((realpix[i-1][j]&0xFF)+(pix[i][j]&0xFF));
			 }
			 else if(choose[i]==3||choose[i]==7){
				 realpix[i][j]=(byte)(((realpix[i-1][j]&0xFF)+(realpix[i][j-3]&0xFF))/2+(pix[i][j]&0xFF));
			 }
			 else{
				 realpix[i][j]=(byte)(((realpix[i-1][j]&0xFF)+(realpix[i][j-3]&0xFF)-(realpix[i-1][j-3]&0xFF))+(pix[i][j]&0xFF));
			 }
		 }
		 }
	 }
	
	int c=0;
	byte[] writepix=new byte[imgBuffer.getHeight()*imgBuffer.getWidth()*3];
	 for(int i=0;i<pix.length;i++){
		 for(int j=0;j<pix[0].length;j++){
			 writepix[c]=realpix[i][j];
			 c++;
		 }
		 
		 }
	/*for(int i=0;i<imgBuffer.getHeight();i++){
    	 for(int j=0;j<imgBuffer.getWidth()*3;j++){
			 System.out.print(realpix[i][j]+"\t");
		 }
		 System.out.println();
	 }*/
for(int i=0;i<choose.length;i++){
		 if(choose[i]==8)
		 System.out.print(" "+choose[i]+"="+i);	 }
	 out.write(writepix);
	 byte[] afterpx=new byte[bytes.length-54-imgBuffer.getWidth()*imgBuffer.getHeight()*3-imgBuffer.getHeight()];
		for(int i=imgBuffer.getWidth()*imgBuffer.getHeight()*3+54+imgBuffer.getHeight();i<bytes.length;i++){
			afterpx[i-(imgBuffer.getWidth()*imgBuffer.getHeight()*3+54+imgBuffer.getHeight())]=bytes[i];
		}
		out.write(afterpx);
		out.close();
}
}
