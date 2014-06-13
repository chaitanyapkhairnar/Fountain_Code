import java.io.*;
import java.util.*;

public class Expander_Encoder {
	public static String CodeGraph_final[];
	public static int codegraph_length;
	public static int codegraph_newlength;
	public static int Seed = 100;
	static PrintWriter writer = null;

	public static void Reverse_CodeGraph(String[] CodeGraph)
	{
		//CodeGraph[0] contains the connections of 0th element from column 1 to the elements in column2
		//bits[1][] contains the bits of 1st byte from file read.
		//Now we need to take bits from each byte at same indexes and encode using the code graph.

		int size_n = 0;
		size_n = (CodeGraphCreate.c*CodeGraphCreate.K)/CodeGraphCreate.d;
		String CodeGraph_n[] = new String[size_n];
		CodeGraph_final = new String[size_n];
		int flag_n[] = new int[size_n];
		for(int i=0;i<size_n;i++)
		{
			flag_n[i] = 0;
		}
		StringTokenizer st;

		for(int i=0;i<codegraph_length;i++)
		{
			String line;
			line = CodeGraph[i];
			//System.out.println("line in code graph is :: " +line);
			writer.println("line in code graph is :: " +line);
			st = new StringTokenizer(line, " ");
			while(st.hasMoreTokens())
			{
				int value = Integer.parseInt(st.nextToken());
				if(flag_n[value] == 0)
				{
					CodeGraph_n[value] = Integer.toString(i);
					CodeGraph_n[value] += " ";
					flag_n[value] = 1;
				}
				else
				{
					CodeGraph_n[value] += Integer.toString(i);
					CodeGraph_n[value] += " ";
				}
			}
		}

		codegraph_newlength = CodeGraph_n.length;

		/*System.out.println("The inverted codegraph is thus :: ");
		for(int i=0;i<codegraph_newlength;i++)
		{
			System.out.println(CodeGraph_n[i]);
		}*/

		for(int i=0;i<codegraph_newlength;i++)
		{
			StringBuilder sb1 = new StringBuilder();
			String line = CodeGraph_n[i];
			StringTokenizer st1 = new StringTokenizer(line, " ");
			String token1 = st1.nextToken();
			sb1.append(token1);
			sb1.append(" ");
			while(st1.hasMoreTokens())
			{
				String token2 = st1.nextToken();
				if(token2.equals(token1))
				{
					token1 = token2;
				}
				else
				{
					sb1.append(token2);
					sb1.append(" ");
					token1 = token2;
				}
			}

			CodeGraph_final[i] = sb1.toString();
		}

		//System.out.println("\n");
		writer.println("\n");
		//System.out.println("Final Codegraph is thus :: ");
		writer.println("Final Codegraph is thus :: ");
		for(int i=0;i<codegraph_newlength;i++)
		{
			//System.out.println(CodeGraph_final[i]);
			writer.println(CodeGraph_final[i]);
		}
	}

	public static String[] final_encode(byte[][] bits)                      
	{
		//System.out.println("Inside the encoding loop");
		writer.println("Inside the encoding Loop");

		int len = CodeGraph_final.length;
		String encoded[] = new String[((int)FileRead.file_length)+len];                            //Number of packets+checksum
		int checksum[][] = new int[len][8];
		int result = 0;

		for(int k=0;k<8;k++)                                        							   //8 because packet length is 8 bits
		{
			for(int i=0;i<len;i++)
			{
				String line = CodeGraph_final[i];
				//System.out.println("line is :: " +line);
				writer.println("line is :: " +line);
				StringTokenizer st = new StringTokenizer(line," ");
				while(st.hasMoreTokens())
				{
					int a = Integer.parseInt(st.nextToken());
					//System.out.println("a is :: " +a +"\n");
					writer.println("a is :: " +a);
					result = exor(bits[a][k], (byte)result);
					//System.out.println("Result is :: " +result);
					writer.println("result is :: " +result);
				}
				checksum[i][k] = result;                                                           // At this point we have checksum value                        
				result = 0;																		   // stored for a particular value of k and all
			}                                                                                      // values of i.
		}

		int bits_len = bits.length;
		int checksum_len = checksum.length;

		for(int k=0;k<bits_len;k++)
		{
			StringBuilder builder = new StringBuilder();
			for(int i=0;i<8;i++)
			{
				builder.append(bits[k][i]);
				builder.append(" ");
			}
			encoded[k] = builder.toString();
		}

		for(int k=0;k<checksum_len;k++)
		{
			StringBuilder builder = new StringBuilder();
			for(int i=0;i<8;i++)
			{
				builder.append(checksum[k][i]);
				builder.append(" ");
			}
			encoded[bits_len+k] = builder.toString();
		}

		int encoded_len = encoded.length;
		
		System.out.println("Encoded Length is :: " +encoded_len);
		
		
		// Now add the header to the packets. 1st bit is data flag which is 1 for data packet and 0 for checksum packet
		// 2nd, 3rd and 4th bits are the sequence bits.
		for(int i=0;i<encoded_len;i++)
		{
			if(i<bits_len)
			{
				encoded[i] = "1 " + decimal2binary(Integer.toString(i)) + encoded[i];
			}
			else
			{
				encoded[i] = "0 " + decimal2binary(Integer.toString(i-bits_len)) + encoded[i];
			}
		}
		

		writer.println("\n\nThe final Encoded data is thus ::\n\n");

		for(int q=0;q<encoded_len;q++) 
		{
			writer.println(encoded[q]);
		}
		return encoded;
	}

	public static byte exor(byte a, byte b)
	{
		byte c;
		if(a==1 & b==0 || a==0 & b==1)
		{
			c=1;
		}
		else
		{
			c=0;
		}
		return(c);
	}

	public static String decimal2binary(String decimal)
	{
		int value = Integer.parseInt(decimal);
		int bit=0;
		StringBuilder st = new StringBuilder();
		while(value>0)
		{
			bit = value%2;
			value = value/2;
			st.append(bit);
			st.append(" ");
			
		}
		String result;
		st.reverse();
		st.append(" ");
		result = st.toString();
		if(result.length()<12)                                                                      // We are considering first 6 bits for length
		{
			int diff = 12-result.length();
			StringBuilder a = new StringBuilder();
			for(int i=0;i<diff;i=i+2)
			{
				a.append("0 ");
			}
			result = a.toString().substring(0, (a.length()-1)) + result;
		}
		else
		{
			result = result.substring(1, result.length());
		}
		return result;
	}
	
	public static void main(String[] args) throws FileNotFoundException, UnsupportedEncodingException
	{
		writer = new PrintWriter("log.txt", "UTF-8");                             				   // To enter data in log file

		System.out.println("Welcome to the encoder !!!");
		String Connections[] = new String[500];
		writer.println("Welcome to encoder !!");                                  				   // Write to the file

		PrintWriter graph = new PrintWriter("graph.txt", "UTF-8");                				   // File for storing the code graph

		Connections = CodeGraphCreate.create_graph(Seed);                                              // Create code graph
		//path = "C:\\Users\\Chaitanya\\workspace\\Fountain_trial\\src\\testV.mp4";
		String path = "C:\\Users\\Chaitanya\\workspace\\Fountain_trial\\src\\abc.txt";             // Path of file to be read bit by bit
		File f = new File(path);
		byte bits[][] = new byte[(int)f.length()][8];						                       // To store bits of file in 2d
		try {
			bits = FileRead.readbytes(path);													   // Read the file
		} catch (IOException e) {
			e.printStackTrace();
		}

		codegraph_length=0;
		codegraph_length = Connections.length;                                                     // Length of the code graph

		System.out.println("\n\n********************************************\n\n");
		writer.println("\n\n********************************************\n\n");
		System.out.println("Now printing from the returned values ::");
		writer.println("Now printing from the returned values ::");
		System.out.println("The length of CodeGraph is >> " +codegraph_length);
		writer.println("The length of CodeGraph is >> " +codegraph_length);
		System.out.println("The CodeGraph is ::");
		writer.println("The CodeGraph is ::");
		PrintWriter writer1 = new PrintWriter("CodeGraph.txt", "UTF-8");
		for(int i=0;i<codegraph_length;i++)
		{
			System.out.println(Connections[i]);
			writer.println(Connections[i]);
			writer1.println(Connections[i]);
		}
		writer.println("The bits read are  ::");

		for(int i=0;i<36;i++)                  													   // Here instead of 36 put the number of bytes in the file
		{
			for(int j=0;j<8;j++)
			{
				writer.println(bits[i][j] + "\t\t");
			}
			writer.println("\n");
		}

		Reverse_CodeGraph(Connections);                                                      // Convert the code graph as per our requirements

		for(int t=0;t<CodeGraph_final.length;t++)
		{
			graph.println(CodeGraph_final[t]);
		}

		String a[] = new String[50];
		a = final_encode(bits);																	   // Do final encoding

		PrintWriter encoded = new PrintWriter("encoded.txt", "UTF-8");
		
		System.out.println("File original packets are :: ");
		for(int i=0;i<bits.length;i++)
		{
			for(int k=0;k<8;k++)
			{
				System.out.print(bits[i][k] + "  ");
			}
			System.out.print("\n");
			
		}
		
		System.out.println("\n\nFinally encoded data is :: ");
		int a_l = a.length;

		for(int i=0;i<a_l;i++)
		{
			System.out.println(a[i]);
			encoded.println(a[i]);
		}
		
		System.out.println("The binary of 32 is ::" +decimal2binary("32"));

		writer.close();
		graph.close();
		encoded.close();
		writer1.close();
	}
}
