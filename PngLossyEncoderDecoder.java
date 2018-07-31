package assign_4;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.imageio.ImageIO;

import com.sun.javafx.image.PixelAccessor;

public class PngLossyEncoderDecoder {
public PngLossyEncoderDecoder() {
	// TODO Auto-generated constructor stub
}
public void Compress(String src) throws IOException{
	double minEntropy = 0,Entropyprev=0,Entropytop=0,Entropyave=0,Entropymid=0,Entropydefult=0;
	BufferedImage imgBuffer = ImageIO.read(new File(src));
	byte[] pxprev = new byte[imgBuffer.getWidth()*3];
	byte[] pxprevnew = new byte[imgBuffer.getWidth()*3];
	byte[][]pxtop = new byte[2][imgBuffer.getWidth()*3];
	byte[] pxtopnew = new byte[imgBuffer.getWidth()*3];
	byte[] pxavenew = new byte[imgBuffer.getWidth()*3];
	byte[] pxmidnew= new byte[imgBuffer.getWidth()*3];
	int [] colorpx=new int[256];
	File file=new File(src);
	 InputStream input = new FileInputStream(file);
	 byte[] bytes=new byte[(int) file.length()];
	 input.read(bytes, 0,bytes.length);
	 Entropytop=PngTop(src, pxtop,pxtopnew);
	/* for(int i=0;i<imgBuffer.getHeight();i++){
	 for(int j=0;j<imgBuffer.getWidth()*3;j++){
		 System.out.print(bytes[imgBuffer.getWidth()*i*3+j+54]+"\t ");
	 }
	 System.out.println();

	 }*/
	 byte[] pixels=new byte[imgBuffer.getWidth()*imgBuffer.getHeight()*3];
     for(int i=0;i<pixels.length;i++){
     	pixels[i]=bytes[i+54];
     }
     /*for(int i=0;i<imgBuffer.getHeight();i++){
    	 for(int j=0;j<imgBuffer.getWidth()*3;j++){
    		 System.out.print(bytes[i*imgBuffer.getWidth()*3+j+54]+" ");
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
			choose=1;
			Entropyprev=PngPrev(src, pxprev,pxprevnew);
			if(Entropyprev<minEntropy){
				choose=1;
				minEntropy=Entropyprev;
			}
			for(int k=1;k>=0;k--){
			for(int j=0;j<imgBuffer.getWidth()*3;j++){
				pxtop[k][j]=pixels[(i-k)*imgBuffer.getWidth()*3+j];
			}
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
			if(Entropymid<minEntropy)
				choose=4;
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
			else{
				out.write(pxmidnew);
			}
			}	
		minEntropy=0;
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
public double PngMid(String src,byte[][] bytes,byte [] newpx) throws IOException{
	int[] colorpx=new int[256];
	 double Entropy=0;
	for(int i=0;i<newpx.length;i++){
		if(i<3){
			newpx[i]=bytes[0][i];
		}else{
		newpx[i]=(byte)((int)bytes[0][i]-(((int)bytes[1][i])+(int)bytes[0][i-3]-(int)(bytes[1][i-3])));}
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
		newpx[i]=(byte)((int)bytes[0][i]-(int)bytes[1][i]);}
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
public double PngPrev(String src,byte[] bytes,byte[] newpx) throws IOException{
	 //byte[] newpx=new byte[bytes.length];
	 int[] colorpx=new int[256];
	 double Entropy=0;
	for(int i=0;i<newpx.length;i++){
		if(i<3){
			newpx[i]=bytes[i];
		}else{
		newpx[i]=(byte)((int)bytes[i]-(int)bytes[i-3]);}
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
		newpx[i]=(byte)((int)bytes[0][i]-(((int)bytes[1][i])+(int)bytes[0][i-3])/2);}
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
	 
	 byte[][] realpix=new byte[imgBuffer.getHeight()][imgBuffer.getWidth()*3];
	 for(int i=0;i<pix.length;i++){
		 for(int j=0;j<pix[0].length;j++){
			 if(j<3){
				 realpix[i][j]=pix[i][j];
			 }
			 else{
			  if(choose[i]==0){
			    realpix[i][j]=(byte)((int)pix[i][j-3]);
			 }
			 else if(choose[i]==1){
				 realpix[i][j]=(byte)((int)realpix[i][j-3]+(int)pix[i][j]);
			 }
			 else if(choose[i]==2){
				 realpix[i][j]=(byte)((int)realpix[i-1][j]+(int)pix[i][j]);
			 }
			 else if(choose[i]==3){
				 realpix[i][j]=(byte)(((int)realpix[i-1][j]+(int)realpix[i][j-3])/2+(int)pix[i][j]);
			 }
			 else{
				 realpix[i][j]=(byte)(((int)realpix[i-1][j]+(int)realpix[i][j-3]-(int)realpix[i-1][j-3])+(int)pix[i][j]);
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
	 for(int i=0;i<choose.length;i++){
		 if(choose[i]==0)
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
