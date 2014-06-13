import java.io.*;

public class decoder {

	public static String[] CodeGraph;
	public static String[] encoded;
	public static int CodeGraph_length = 0;
	public static int error_flag = 0;
	public static int Seed = 100;


	public static void main(String[] args) throws IOException
	{
		System.out.println("This is decoder function.");

		//Let we assume that we have a codegraph and also the encoded bits in a string array.

		BufferedReader br = new BufferedReader(new FileReader("graph.txt"));
		int line_index = 0;
		String line_i;
		String[] line = new String[500];
		line_i = br.readLine();
		line[line_index] = line_i;
		line_index++;
		while(line_i != null)
		{
			line_i = br.readLine();
			line[line_index] = line_i;
			line_index++;
		}

		line_index = line_index-1;
		System.out.println("line count is :: " +line_index);
		CodeGraph = new String[line_index];
		for(int i=0;i<line_index;i++)
		{
			CodeGraph[i] = line[i];
		}

		System.out.println("The CodeGraph is thus :: ");
		for(int i=0;i<line_index;i++)
		{
			System.out.println(CodeGraph[i]);
		}

		br.close();

		BufferedReader coder = new BufferedReader(new FileReader("encoded.txt"));
		encoded = new String[9];
		String line_encoded = coder.readLine();
		int encoded_index = 0;
		encoded[encoded_index] = line_encoded;
		encoded_index++;
		while(line_encoded != null)
		{
			line_encoded = coder.readLine();
			encoded[encoded_index] = line_encoded;
			encoded_index++;
		}

		System.out.println("the encoded string is thus :: ");

		for(int i=0;i<8;i++)
		{
			System.out.println(encoded[i]);
		}

		coder.close();
		// Till here we got the codegraph and the encoded bits from the file ... Now we start the decoding process ...

		decode();

	}

	public static void decode() throws FileNotFoundException, UnsupportedEncodingException
	{
		CodeGraph_length = CodeGraph.length;

		String encoded_items[][] = new String[8][];
		for(int i=0;i<8;i++)											//Tokenize the encoded data into individual items
		{
			String encoded_line = encoded[i];   //get line by line
			encoded_items[i] = encoded_line.split(" ");  //do items[0].length to get length
		}

		/*	for(int i=0;i<8;i++)
		{
			for(int j=0;j<48;j++)
			{
				System.out.print(items[i][j]);
				System.out.print("\t\t");
			}
			System.out.print("\n");
		}*/
		String CodeGraph_items[][] = new String[CodeGraph_length][];
		for(int i=0;i<CodeGraph_length;i++)								//Tokenize the CodeGraph into individual items
		{
			String CodeGraph_line = CodeGraph[i];
			CodeGraph_items[i] = CodeGraph_line.split(" ");

		}

		String xor_result[][] = new String[8][CodeGraph_length];
		for(int k=0;k<8;k++)
		{	
			for(int i=0;i<CodeGraph_length;i++)								//Tokenize the CodeGraph into individual items
			{
				int len = CodeGraph_items[i].length;
				String result = "0";
				for(int j=0;j<len;j++)
				{
					result = exor(encoded_items[k][Integer.parseInt(CodeGraph_items[i][j])], result);
					//System.out.println("The data is :: " + encoded_items[k][Integer.parseInt(CodeGraph_items[i][j])]);
				}
				xor_result[k][i] = result;
				//System.out.println("result is thus :: " +result);
			}
		}

		System.out.println("\nThe xor results are :: \n");

		for(int i=0;i<8;i++)
		{
			for(int j=0;j<CodeGraph_length;j++)
			{
				System.out.print(xor_result[i][j]);
				System.out.print("\t\t");
			}
			System.out.print("\n");
		}

		//Now comparing the checksum bits from encoded data with the xor_result

		int len = encoded_items[0].length;
		for(int i=0;i<8;i++)
		{
			
			for(int j=0;j<CodeGraph_length;j++)
			{
				if(xor_result[i][j].equals(encoded_items[i][j+((len-CodeGraph_length))]))
				{
					//System.out.println("No errors found !!");
				}
				else
				{
					//System.out.println("There are errors !!");
					error_flag = 1;
				}
			}
		}
		
		if(error_flag == 1)
		{
			System.out.println("Errors are present !!");
		}
		else
		{
			System.out.println("Errors are not present !!");
		}
		
		//Now we reassemble the packets to recreate the file.
		
		String packets[] = new String[(len-CodeGraph_length)];
		System.out.println("len-CodeGraph_len is :: " +(len-CodeGraph_length));
		System.out.println("The length of encoded_items is " +encoded_items.length + " by " +encoded_items[0].length);
		int packets_index = 0;
		
		for(int j=0;j<(len-CodeGraph_length);j++)
		{
			StringBuilder st = new StringBuilder();

			for(int i=0;i<8;i++)
			{
				st.append(encoded_items[i][j]);
				//st.append(" ");
			}
			packets[packets_index] = st.toString();
			packets_index++;
		}

		int final_packets[] = new int[packets.length];
		
		System.out.println("The packets recovered are thus :: ");
		for(int i=0;i<(packets.length);i++)
		{
			System.out.print(packets[i]);
			StringBuilder st1 = new StringBuilder(packets[i]);
			st1.reverse();													//reverse the string as bits are from LSB to MSB
			packets[i] = st1.toString();
			System.out.print("\t\t\t" +packets[i]);
			final_packets[i] = Integer.parseInt(packets[i], 2);
			System.out.print("\t\t\t" +final_packets[i] + "\n");
		}
		
		PrintWriter writer = new PrintWriter("recreated_abc.txt", "UTF-8");   // File name changes according to the format of the input file
		for(int i=0;i<(packets.length);i++)
		{
			writer.print((char)final_packets[i]);
		}
		writer.close();
	}
	
	public static String exor(String a, String b)
	{
		String c;
		if(a.equals("1") & b.equals("0") || a.equals("0") & b.equals("1"))
		{
			c="1";
		}
		else
		{
			c="0";
		}
		return(c);
	}
}
