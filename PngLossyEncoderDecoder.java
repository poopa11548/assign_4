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
public void Compress(String src,int LevelSelected) throws IOException{
	double minEntropy = 0,Entropyprev=0,Entropytop=0,Entropyave=0,EntropyaveLossy=0,Entropymid=0,EntropymidLossy=0,Entropydefult=0,EntropyprevLossy=0,EntropytopLossy=0;
	BufferedImage imgBuffer = ImageIO.read(new File(src));
	File file=new File(src);
	 InputStream input = new FileInputStream(file);
	 byte[] bytes=new byte[(int) file.length()];
	 input.read(bytes, 0,bytes.length);
	 byte[][]pxtop = new byte[2][imgBuffer.getWidth()*((int)bytes[28]/8)];
		byte[] pxprev = new byte[imgBuffer.getWidth()*((int)bytes[28]/8)];
		byte[] pxprevnew = new byte[imgBuffer.getWidth()*((int)bytes[28]/8)];
		byte[] pxtopnew = new byte[imgBuffer.getWidth()*((int)bytes[28]/8)];
		byte[] pxavenew = new byte[imgBuffer.getWidth()*((int)bytes[28]/8)];
		byte[] pxmidnew= new byte[imgBuffer.getWidth()*((int)bytes[28]/8)];
		byte[] pxprevnewL = new byte[imgBuffer.getWidth()*((int)bytes[28]/8)];
		byte[] pxtopnewL = new byte[imgBuffer.getWidth()*((int)bytes[28]/8)];
		byte[] pxavenewL= new byte[imgBuffer.getWidth()*((int)bytes[28]/8)];
		byte[] pxmidnewL= new byte[imgBuffer.getWidth()*((int)bytes[28]/8)];
		int [] colorpx=new int[256];
		int [] colorpxtotal=new int [256];
	 ///Entropytop=PngTop(src, pxtop,pxtopnew,colorpxtotal,i);
	 byte[] pixels=new byte[imgBuffer.getWidth()*imgBuffer.getHeight()*((int)bytes[28]/8)];
     for(int i=0;i<pixels.length;i++){
     	pixels[i]=bytes[i+54];
     }
  /* for(int i=0;i<imgBuffer.getHeight();i++){
    	 for(int j=0;j<imgBuffer.getWidth()*3;j++){
    		 System.out.print(pixels[i*imgBuffer.getWidth()*3+j]+"\t");
    	 }
    	 System.out.println();
     }*/
	File file2=new File(src.substring(0, src.length()-3)+"LYM-prev");
    OutputStream out=new FileOutputStream(file2);
    byte choose=0;
    byte[] beforepx=new byte[54];
    for(int i=0;i<54;i++){
    	beforepx[i]=bytes[i];
    }
    out.write(beforepx);
	for(int i=0;i<imgBuffer.getHeight();i++){
		if(i==0){
		for(int j=0;j<imgBuffer.getWidth()*((int)bytes[28]/8);j++){
			pxprev[j]=pixels[i*imgBuffer.getWidth()*((int)bytes[28]/8)+j];
			
		}
		Entropyprev=PngPrev(((int)bytes[28]/8), pxprev,pxprevnew,colorpxtotal,i);
		choose=1;
		out.write(choose);
		out.write(pxprevnew);
		for(int j=0;j<imgBuffer.getWidth()*((int)bytes[28]/8);j++){
			pxtop[1][j]=pixels[(i)*imgBuffer.getWidth()*((int)bytes[28]/8)+j];
		}
		}
		else{
			for(int j=0;j<imgBuffer.getWidth()*((int)bytes[28]/8);j++){
				pxprev[j]=pixels[i*imgBuffer.getWidth()*((int)bytes[28]/8)+j];
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
			for(int j=0;j<imgBuffer.getWidth()*((int)bytes[28]/8);j++){
				pxtop[0][j]=pixels[(i)*imgBuffer.getWidth()*((int)bytes[28]/8)+j];
			}
			//minEntropy=999999999;
			if(LevelSelected==0){
			choose=1;
			//minEntropy=999999999;
			Entropyprev=PngPrev(((int)bytes[28]/8), pxprev,pxprevnew,colorpxtotal,i);
			if(Entropyprev<minEntropy){
				choose=1;
				minEntropy=Entropyprev;
			}
		    Entropytop=PngTop(((int)bytes[28]/8), pxtop,pxtopnew,colorpxtotal,i);	
			if(Entropytop<minEntropy){
				choose=2;
				minEntropy=Entropytop;
			}
			Entropyave=PngAve(((int)bytes[28]/8),pxtop,pxavenew,colorpxtotal,i);
			if(Entropyave<minEntropy){
				choose=3;
				minEntropy=Entropyave;
			}
			Entropymid=PngMid(((int)bytes[28]/8),pxtop,pxmidnew,colorpxtotal,i);
			if(Entropymid<minEntropy){
				choose=4;
				minEntropy=Entropymid;
			}
			}
			else{
				minEntropy=999999999;
		   EntropyprevLossy=PngPrevLossy(((int)bytes[28]/8), pxprev, pxprevnewL,colorpxtotal,i,(100/LevelSelected));
			if(EntropyprevLossy<minEntropy){
				choose=5;
				minEntropy=EntropyprevLossy;
			}
		   EntropytopLossy=PngTopLossy(((int)bytes[28]/8), pxtop,pxtopnewL,colorpxtotal,i,(100/LevelSelected));
			if(EntropytopLossy<minEntropy){
				choose=6;
				minEntropy=EntropytopLossy;
			}
			EntropyaveLossy=PngAveLossy(((int)bytes[28]/8),pxtop,pxavenewL,colorpxtotal,i,(100/LevelSelected));
		if(EntropyaveLossy<minEntropy){
				choose=7;
				minEntropy=EntropyaveLossy;
			}
		//System.out.print(minEntropy+"="+i);
		EntropymidLossy=PngMidLossy(((int)bytes[28]/8), pxtop,pxmidnewL,colorpxtotal,i,(100/LevelSelected));
		if(EntropymidLossy<minEntropy){
			choose=8;
			minEntropy=EntropymidLossy;
		}
			}
		//System.out.print(minEntropy+"="+i);
		
			out.write(choose);
			if(choose==0){
				out.write(pxprev);
				for(int j=0;j<pxprev.length;j++){
					colorpxtotal[pxprev[j]&0xFF]++;
				}
			}
			else if(choose==1){
			out.write(pxprevnew);
			for(int j=0;j<pxprevnew.length;j++){
				colorpxtotal[pxprevnew[j]&0xFF]++;
			}
			}
			else if(choose==2){
				out.write(pxtopnew);
				for(int j=0;j<pxtopnew.length;j++){
					colorpxtotal[pxtopnew[j]&0xFF]++;
				}
			}
			else if(choose==3){
				out.write(pxavenew);
				for(int j=0;j<pxavenew.length;j++){
					colorpxtotal[pxavenew[j]&0xFF]++;
				}
			}
			else if(choose==4){
				out.write(pxmidnew);
				for(int j=0;j<pxmidnew.length;j++){
					colorpxtotal[pxmidnew[j]&0xFF]++;
				}
			}
			else if (choose==5){
				out.write(pxprevnewL);
				for(int j=0;j<pxprevnewL.length;j++){
					colorpxtotal[pxprevnewL[j]&0xFF]++;
				}
			}
			else if(choose==6){
				out.write(pxtopnewL);
				for(int j=0;j<pxtopnewL.length;j++){
					colorpxtotal[pxtopnewL[j]&0xFF]++;
				}
			}
			else if(choose==7){
				out.write(pxavenewL);
				for(int j=0;j<pxavenewL.length;j++){
					colorpxtotal[pxavenewL[j]&0xFF]++;
				}
			}
			else if(choose==8){
				out.write(pxmidnewL);
				for(int j=0;j<pxmidnewL.length;j++){
					colorpxtotal[pxmidnewL[j]&0xFF]++;
				}
			}
			
			
			}	
		if(choose>=0&&choose<5){
			for(int j=0;j<imgBuffer.getWidth()*((int)bytes[28]/8);j++){
				pxtop[1][j]=pixels[(i)*imgBuffer.getWidth()*((int)bytes[28]/8)+j];
			}
		}
		else if(choose==5) {
			for(int j=0;j<imgBuffer.getWidth()*((int)bytes[28]/8);j++){
				if(j<((int)bytes[28]/8))
					pxtop[1][j]=pixels[(i)*imgBuffer.getWidth()*((int)bytes[28]/8)+j];
		    	else{
		    		pxtop[1][j]=(byte) ((pxtop[1][j-((int)bytes[28]/8)]&0xFF)+(pxprevnewL[j]&0xFF));
		    	}
			}
		}
		else if(choose==6) {
			for(int j=0;j<imgBuffer.getWidth()*((int)bytes[28]/8);j++){
				if(j<((int)bytes[28]/8))
					pxtop[1][j]=pixels[(i)*imgBuffer.getWidth()*((int)bytes[28]/8)+j];
		    	else{
		    		pxtop[1][j]=(byte) ((pxtop[1][j]&0xFF)+(pxtopnewL[j]&0xFF));
		    	}
			}
		}
		else if(choose==7){
			for(int j=0;j<imgBuffer.getWidth()*((int)bytes[28]/8);j++){
				if(j<((int)bytes[28]/8))
					pxtop[1][j]=pixels[(i)*imgBuffer.getWidth()*((int)bytes[28]/8)+j];
		    	else{
		    		pxtop[1][j]=(byte) (((pxtop[1][j]&0xFF)+(pxtop[1][j-((int)bytes[28]/8)]&0xFF))/2+(pxavenewL[j]&0xFF));
		    	}
				//System.out.println((byte) (((int)pxtop[1][j]+(int)pxtop[1][j-3])/2+(int)pxavenewL[j]));
			}
		}
		else if(choose==8){
			byte[] temppx=new byte[pxtop[1].length];
			for(int k=0;k<temppx.length;k++)
				temppx[k]=pxtop[1][k];
			for(int j=0;j<imgBuffer.getWidth()*((int)bytes[28]/8);j++){
				if(j<((int)bytes[28]/8))
					pxtop[1][j]=pixels[(i)*imgBuffer.getWidth()*((int)bytes[28]/8)+j];
		    	else{
		    		pxtop[1][j]=(byte) (((pxtop[1][j]&0xFF)+(pxtop[1][j-((int)bytes[28]/8)]&0xFF)-(temppx[j-((int)bytes[28]/8)]&0xFF))+(pxmidnewL[j]&0xFF));
		    	}
			}
		}
		
		//minEntropy=0;
		for(int j=0;j<256;j++){
			colorpx[j]=0;
		}
	}
	byte[] afterpx=new byte[bytes.length-54-imgBuffer.getWidth()*imgBuffer.getHeight()*((int)bytes[28]/8)];
	for(int i=imgBuffer.getWidth()*imgBuffer.getHeight()*((int)bytes[28]/8)+54;i<bytes.length;i++){
		afterpx[i-(imgBuffer.getWidth()*imgBuffer.getHeight()*((int)bytes[28]/8)+54)]=bytes[i];
	}
	out.write(afterpx);
	HufmannEncoderDecoder x=new HufmannEncoderDecoder();
	String[] s=new String[2];
	s[0]=src.substring(0, src.length()-3)+"LYM-prev";
	String[] g=new String[2];
	g[0]=src.substring(0, src.length()-3)+"LYM";
	x.CompressWithArray(s,g);
	out.close();
	file2.delete();
	input.close();
}
public double PngMidLossy(int deep,byte[][] bytes,byte [] newpx,int[] colorpxtotal,int k,int size) throws IOException{
	int[] colorpx=new int[256];
	 double Entropy=0;
	 byte x=0;
	 int count=0;
	 for(int j=0;j<deep;j++){
	for(int i=0;i<newpx.length;i=i+deep){
		if(i<deep){
			newpx[i+j]=bytes[0][i+j];
			x=bytes[0][i+j];
		}else{
			if(((bytes[0][i+j]&0xFF)-((bytes[1][i+j]&0xFF)+(x&0xFF)-(bytes[1][i+j-deep]&0xFF)))<size&&((bytes[0][i+j]&0xFF)-((bytes[1][i+j]&0xFF)+(x&0xFF)-(bytes[1][i+j-deep]&0xFF)))>-size){
		    newpx[i+j]=(byte)((bytes[0][i+j]&0xFF)-((bytes[1][i+j]&0xFF)+(x&0xFF)-(bytes[1][i+j-deep]&0xFF)));
		    x=bytes[0][i+j];
			}
			else if(((bytes[0][i+j]&0xFF)-((bytes[1][i+j]&0xFF)+(x&0xFF)-(bytes[1][i+j-deep]&0xFF)))<size){
				newpx[i+j]=(byte)-size;
				x=(byte)(((bytes[1][i+j]&0xFF)+(x&0xFF)-(bytes[1][i+j-deep]&0xFF))-(size));
				//if((bytes[0][i+j]&0xFF)-x>50||(bytes[0][i+j]&0xFF)-x<-50)
				  count++;
			}else{
				newpx[i+j]=(byte)+size;
				x=(byte)(((bytes[1][i+j]&0xFF)+(x&0xFF)-(bytes[1][i+j-deep]&0xFF))+(size));
				//if((bytes[0][i+j]&0xFF)-x>50||(bytes[0][i+j]&0xFF)-x<-50)
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
			if(colorpx[i]!=0||colorpxtotal[i]!=0){
				Entropy+=((double)(colorpx[i]+colorpxtotal[i])/((k+1)*newpx.length))*((Math.log(((double)(colorpx[i]+colorpxtotal[i])/((k+1)*newpx.length))) / Math.log(2)))*-1;
			}
		}
	 /*f(k==144)
	 System.out.print(count+" ");*/
	/*if(count<newpx.length*0.01&&size>10)
		PngMidLossy(src,bytes,newpx,colorpxtotal,k,size-10);*/
	//else 
	 if(size==100){
		 if(count>newpx.length*0.1){
				 Entropy=999999998;
			}
		 else{
			 if(count>newpx.length*0.2){
					 Entropy=999999998;
				}
		 }
		 }
	return Entropy;
	
}

public double PngAveLossy(int deep,byte[][] bytes,byte [] newpx,int[] colorpxtotal,int k,int size) throws IOException{
	int[] colorpx=new int[256];
	 double Entropy=0;
	 byte x=0;
	 int count=0;
	 for(int j=0;j<deep;j++){
	for(int i=0;i<newpx.length;i=i+deep){
		if(i<deep){
			newpx[i+j]=bytes[0][i+j];
			x=bytes[0][i+j];
		}else{
			if(((bytes[0][i+j]&0xFF)-((bytes[1][i+j]&0xFF)+(x&0xFF))/2)<size&&((bytes[0][i+j]&0xFF-(bytes[1][i+j]&0xFF+(x&0xFF))/2)>-size)){
				newpx[i+j]=(byte)((bytes[0][i+j]&0xFF)-(((bytes[1][i+j]&0xFF)+(x&0xFF))/2));
				x=bytes[0][i+j];
			}
			else if(((bytes[0][i+j]&0xFF)-((bytes[1][i+j]&0xFF+(x&0xFF))/2))<size){
				newpx[i+j]=(byte)-size;
				x=(byte)(((x&0xFF)+(bytes[1][i+j]&0xFF))/2-(size));
				count++;
			}else{
				newpx[i+j]=(byte)+size;
				x=(byte)(((x&0xFF)+(bytes[1][i+j]&0xFF))/2+(size));
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
			if(colorpx[i]!=0||colorpxtotal[i]!=0){
				Entropy+=((double)(colorpx[i]+colorpxtotal[i])/((k+1)*newpx.length))*((Math.log(((double)(colorpx[i]+colorpxtotal[i])/((k+1)*newpx.length))) / Math.log(2)))*-1;
			}
		}
	 /*if(count<newpx.length*0.01&&size>10)
			PngAveLossy(src,bytes,newpx,colorpxtotal,k,size-10);*/
	 if(size==100){
		 if(count>newpx.length*0.1){
				 Entropy=999999998;
			}
		 else{
			 if(count>newpx.length*0.2){
					 Entropy=999999998;
				}
		 }
		 }
	return Entropy;
	
}
public double PngTopLossy(int deep,byte[][] bytes,byte [] newpx,int[] colorpxtotal,int k,int size) throws IOException{
	
	int[] colorpx=new int[256];
	 double Entropy=0;
	 int count=0;
	for(int i=0;i<newpx.length;i++){
		if(i<deep){
			newpx[i]=bytes[0][i];
		}
		else{
			if(((bytes[0][i]&0xFF)-(bytes[1][i]&0xFF))<size&&((bytes[0][i]&0xFF)-(bytes[1][i]&0xFF)>-size)){
				newpx[i]=(byte)((bytes[0][i]&0xFF)-(bytes[1][i]&0xFF));
			}
			else if(((bytes[0][i]&0xFF)-(bytes[1][i]&0xFF))<size){
				newpx[i]=(byte)-size;
				count++;
			}else{
				newpx[i]=(byte)size;
				count++;
			}
		}	
		if(newpx[i]<0)
   		colorpx[newpx[i]+256]++;
   	else
   		colorpx[newpx[i]]++;
	}
	for(int i=0;i<256;i++){
		if(colorpx[i]!=0||colorpxtotal[i]!=0){
			Entropy+=((double)(colorpx[i]+colorpxtotal[i])/((k+1)*newpx.length))*((Math.log((double)(colorpx[i]+colorpxtotal[i])/((k+1)*newpx.length)) / Math.log(2)))*-1;
		}
	}
	/*if(count<newpx.length*0.01&&size>10)
		PngTopLossy(src,bytes,newpx,colorpxtotal,k,size-10);*/
	if(size==100){
		 if(count>newpx.length*0.1){
				 Entropy=999999998;
			}
		 else{
			 if(count>newpx.length*0.2){
					 Entropy=999999998;
				}
		 }
		 }
	return Entropy;
	
}
public double PngPrevLossy(int deep,byte[] bytes,byte[] newpx,int[] colorpxtotal,int k,int size) throws IOException{
	 //byte[] newpx=new byte[newpx.length];
	 int[] colorpx=new int[256];
	 double Entropy=0;
	 byte x=0;
	 int count=0;
	 for(int j=0;j<deep;j++){
	    for(int i=0;i<bytes.length;i=i+deep){
				if(i<deep){
					newpx[i+j]=bytes[i+j];
					x=bytes[i+j];
				}
				else{
					if(((bytes[i+j]&0xFF)-(x&0xFF))<size&&((bytes[i+j]&0xFF)-(x&0xFF)>-size)){
						newpx[i+j]=(byte)((bytes[i+j]&0xFF)-(x&0xFF));
						x=(byte)((bytes[i+j]&0xFF));
					}
					else if((bytes[i+j]&0xFF)-(x&0xFF)<size){
						newpx[i+j]=(byte)-size;
						x=(byte)((x&0xFF)-size);
						count++;
					}else{
						newpx[i+j]=(byte)size;
						x=(byte)((x&0xFF)+size);
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
			if(colorpx[i]!=0||colorpxtotal[i]!=0){
				Entropy+=((double)(colorpx[i]+colorpxtotal[i])/((k+1)*newpx.length))*((Math.log((double)(colorpx[i]+colorpxtotal[i])/((k+1)*newpx.length)) / Math.log(2)))*-1;
			}
		}
	 /*if(count<newpx.length*0.01&&size>10)
			PngPrevLossy(src,bytes,newpx,colorpxtotal,k,size-10);*/
	 if(size==100){
	 if(count>newpx.length*0.1){
			 Entropy=999999998;
		}
	 else{
		 if(count>newpx.length*0.2){
				 Entropy=999999998;
			}
	 }
	 }
	return Entropy;
}
public double PngMid(int deep,byte[][] bytes,byte [] newpx,int[] colorpxtotal,int k) throws IOException{
	int[] colorpx=new int[256];
	 double Entropy=0;
	for(int i=0;i<newpx.length;i++){
		if(i<deep){
			newpx[i]=bytes[0][i];
		}else{
		newpx[i]=(byte)((bytes[0][i]&0xFF)-((bytes[1][i]&0xFF)+(bytes[0][i-deep]&0xFF)-(bytes[1][i-deep]&0xFF)));}
		if(newpx[i]<0)
   		colorpx[newpx[i]+256]++;
   	else
   		colorpx[newpx[i]]++;
	}
	for(int i=0;i<256;i++){
		if(colorpx[i]!=0||colorpxtotal[i]!=0){
			Entropy+=((double)(colorpx[i]+colorpxtotal[i])/((k+1)*newpx.length))*((Math.log((double)(colorpx[i]+colorpxtotal[i])/((k+1)*newpx.length)) / Math.log(2)))*-1;
		}
	}
	
	return Entropy;
	
}
public double PngTop(int deep,byte[][] bytes,byte [] newpx,int[] colorpxtotal,int k) throws IOException{
	
	int[] colorpx=new int[256];
	 double Entropy=0;
	for(int i=0;i<newpx.length;i++){
		if(i<deep){
			newpx[i]=bytes[0][i];
		}else{
		newpx[i]=(byte)((bytes[0][i]&0xFF)-(bytes[1][i]&0xFF));}
		if(newpx[i]<0)
   		colorpx[newpx[i]+256]++;
   	else
   		colorpx[newpx[i]]++;
	}
	for(int i=0;i<256;i++){
		if(colorpx[i]!=0||colorpxtotal[i]!=0){
			Entropy+=((double)(colorpx[i]+colorpxtotal[i])/((k+1)*newpx.length))*((Math.log((double)(colorpx[i]+colorpxtotal[i])/((k+1)*newpx.length)) / Math.log(2)))*-1;
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

public double PngPrev(int deep,byte[] bytes,byte[] newpx,int[] colorpxtotal,int k) throws IOException{
	 //byte[] newpx=new byte[bytes.length];
	 int[] colorpx=new int[256];
	 double Entropy=0;
	for(int i=0;i<newpx.length;i++){
		if(i<deep){
			newpx[i]=bytes[i];
		}else{
		newpx[i]=(byte)((bytes[i]&0xFF)-(bytes[i-deep]&0xFF));}
		if(newpx[i]<0)
    		colorpx[newpx[i]+256]++;
    	else
    		colorpx[newpx[i]]++;
	}
	for(int i=0;i<256;i++){
		if(colorpx[i]!=0||colorpxtotal[i]!=0){
			Entropy+=((double)(colorpx[i]+colorpxtotal[i])/((k+1)*newpx.length))*((Math.log((double)(colorpx[i]+colorpxtotal[i])/((k+1)*newpx.length)) / Math.log(2)))*-1;
		}
	}
	return Entropy;
}
public double PngAve(int deep,byte[][] bytes,byte [] newpx,int[] colorpxtotal,int k) throws IOException{
	
	int[] colorpx=new int[256];
	 double Entropy=0;
	for(int i=0;i<newpx.length;i++){
		if(i<deep){
			newpx[i]=bytes[0][i];
		}else{
		newpx[i]=(byte)((bytes[0][i]&0xFF)-((bytes[1][i]&0xFF)+(bytes[0][i-deep]&0xFF))/2);}
		if(newpx[i]<0)
   		colorpx[newpx[i]+256]++;
   	else
   		colorpx[newpx[i]]++;
	}
	for(int i=0;i<256;i++){
		if(colorpx[i]!=0||colorpxtotal[i]!=0){
			Entropy+=((double)(colorpx[i]+colorpxtotal[i])/((k+1)*newpx.length))*((Math.log((double)(colorpx[i]+colorpxtotal[i])/((k+1)*newpx.length)) / Math.log(2)))*-1;
		}
	}
	return Entropy;
	
}

public void Decompress(String src) throws IOException{
	HufmannEncoderDecoder x=new HufmannEncoderDecoder();
	String [] s=new String[2];
	s[0]=src;
	String [] g=new String[2];
	g[0]=src.substring(0, src.length()-3)+"LYMM-prev";
	x.Decompress(s,g);
	BufferedImage imgBuffer = ImageIO.read(new File(src.substring(0, src.length()-3)+"LYMM-prev"));
	File file=new File(src.substring(0, src.length()-3)+"LYMM-prev");
	 InputStream input = new FileInputStream(file);
	 byte[] bytes=new byte[(int) file.length()];
	 input.read(bytes, 0,bytes.length);
	 byte[][] pix=new byte[imgBuffer.getHeight()][imgBuffer.getWidth()*((int)bytes[28]/8)];
	 byte[] beforpx=new byte[54];
	 input.close();
	 file.delete();
	 File file2=new File(src.substring(0, src.length()-4)+"-LYM.bmp");
	    OutputStream out=new FileOutputStream(file2);
	 for(int i=0;i<54;i++){
		 beforpx[i]=bytes[i];
	 }
	 out.write(beforpx);
	 byte[] choose=new byte[imgBuffer.getHeight()];
	 for(int i=0;i<imgBuffer.getHeight();i++){
		 choose[i]=bytes[i*imgBuffer.getWidth()*((int)bytes[28]/8)+54+i];
		 for(int j=0;j<imgBuffer.getWidth()*((int)bytes[28]/8);j++){
			 pix[i][j]=bytes[i*imgBuffer.getWidth()*((int)bytes[28]/8)+j+i+54+1];
		 }
	 }
	  /*  for(int i=0;i<imgBuffer.getHeight();i++){
		 for(int j=0;j<imgBuffer.getWidth()*3;j++){
			 System.out.print(pix[i][j]+"\t ");
		 }
		 System.out.println();

		 }*/
	 
	 byte[][] realpix=new byte[imgBuffer.getHeight()][imgBuffer.getWidth()*((int)bytes[28]/8)];
	 boolean f=true;
	 byte ff=0;
	 for(int i=0;i<pix.length;i++){
		 for(int j=0;j<pix[0].length;j++){
			 if(j<((int)bytes[28]/8)){
				 realpix[i][j]=pix[i][j];
			 }
			 else{
			  if(choose[i]==0){
			    realpix[i][j]=(byte)(pix[i][j-((int)bytes[28]/8)]&0xFF);
			 }
			 else if(choose[i]==1||choose[i]==5){
				 realpix[i][j]=(byte)((realpix[i][j-((int)bytes[28]/8)]&0xFF)+(pix[i][j]&0xFF));
			 }
			 else if(choose[i]==2||choose[i]==6){
				 realpix[i][j]=(byte)((realpix[i-1][j]&0xFF)+(pix[i][j]&0xFF));
			 }
			 else if(choose[i]==3||choose[i]==7){
				 realpix[i][j]=(byte)(((realpix[i-1][j]&0xFF)+(realpix[i][j-((int)bytes[28]/8)]&0xFF))/2+(pix[i][j]&0xFF));
			 }
			 else{
				 realpix[i][j]=(byte)(((realpix[i-1][j]&0xFF)+(realpix[i][j-((int)bytes[28]/8)]&0xFF)-(realpix[i-1][j-((int)bytes[28]/8)]&0xFF))+(pix[i][j]&0xFF));
			 }
		 }
		 }
	 }
	
	int c=0;
	byte[] writepix=new byte[imgBuffer.getHeight()*imgBuffer.getWidth()*((int)bytes[28]/8)];
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
	 }
for(int i=0;i<choose.length;i++){
		 if(choose[i]<5)
		 System.out.print(" "+choose[i]+"="+i);	 }*/
	 out.write(writepix);
	 byte[] afterpx=new byte[bytes.length-54-imgBuffer.getWidth()*imgBuffer.getHeight()*((int)bytes[28]/8)-imgBuffer.getHeight()];
		for(int i=imgBuffer.getWidth()*imgBuffer.getHeight()*((int)bytes[28]/8)+54+imgBuffer.getHeight();i<bytes.length;i++){
			afterpx[i-(imgBuffer.getWidth()*imgBuffer.getHeight()*((int)bytes[28]/8)+54+imgBuffer.getHeight())]=bytes[i];
		}
		out.write(afterpx);
		out.close();
}
}
