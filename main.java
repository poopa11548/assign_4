package assign_4;

import java.io.IOException;

public class main {

	public static void main(String[] args) throws IOException {
		PngLossyEncoderDecoder x=new PngLossyEncoderDecoder();
		x.Compress("c:\\data\\bg.bmp");
//System.out.println((int)(Math.log(8)/Math.log(2)));
		x.Decompress("c:\\data\\bg.LIM");
	}

}
