package assign_4;

import java.io.IOException;

public class main {

	public static void main(String[] args) throws IOException {
		PngLossyEncoderDecoder x=new PngLossyEncoderDecoder();
		long time= System.currentTimeMillis();
		x.Compress("c:\\data\\untitled15.bmp",2);
		System.out.println("compress="+(double)(System.currentTimeMillis()-time)/1000);
//System.out.println((int)(Math.log(8)/Math.log(2)));
		System.out.println("comp");
		x.Decompress("c:\\data\\untitled15.LYM");
		System.out.println("total="+(double)(System.currentTimeMillis()-time)/1000);
	}

}
