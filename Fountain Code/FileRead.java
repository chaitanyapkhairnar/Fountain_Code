import java.io.*;


public class FileRead {

	public static byte ID;
	public static byte bits[][];
	public static long file_length = 0;

	public static byte[][] readbytes(String filename) throws IOException
	{
		System.out.println("Inside File read method");
		
		File f = new File(filename);
		FileInputStream fis = new FileInputStream(filename);
		int offset = 0;

		file_length = f.length();                                                   //length has number of bytes in our case it is packet count
		System.out.println("File length is :: " +file_length);
		byte[] bytes = new byte[(int)file_length];

		for(int i=0;i<file_length;i++)
		{
			fis.read(bytes, offset, 1);   											// the 1 is how many bytes to read from file
			offset++;
		}

		/*System.out.println("Bytes read are :: " +bytesRead);
		System.out.println("The data read is :: ");
		for(int i=0;i<file_length;i++)
		{
			System.out.println(bytes[i]);                                           //bytes array contains the packets of input file 1byte each
		}*/

		//bits = new byte[(int)file_length][(int)file_length*8];                      //To keep track of bits from each packet
		bits = new byte[(int)file_length][8];
		int position=0;

		for(int j=0;j<(int)file_length;j++)
		{
			ID = bytes[j];                                                          //8 bits in a byte so loop till k<8
			for(int k=0;k<8;k++)
			{
				position = k;
				bits[j][k] = getBit(position);                                      //bits[3][0] contains the LSB bit of 3rd byte and
			}                                                                       //bits[3][7] contains the MSB bit of 3rd byte
		}

		/*System.out.println("The list of bit values for byte " +bytes[20] + " is ::");
		for(int z=0;z<8;z++)
		{
			System.out.println(bits[20][z]);
		}
		 */

		fis.close();
		return bits;
	}

	public static byte getBit(int position)                                         //Function for getting the bit value at given location
	{																				//from the byte
		return (byte) ((ID >> position) & 1);
	}

	public static void main(String[] args) throws IOException
	{
		String path;
		//path = "C:\\Users\\Chaitanya\\workspace\\Fountain_trial\\src\\testV.mp4";
		path = "C:\\Users\\Chaitanya\\workspace\\Fountain_trial\\src\\abc.txt";
		bits = readbytes(path);
	}
}
