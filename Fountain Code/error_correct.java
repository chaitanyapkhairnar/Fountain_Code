import java.io.*;


public class error_correct {

	public static String[] CodeGraph;
	public static String[] CodeGraph_original;
	public static String[] encoded;
	public static int CodeGraph_length = 0;
	public static int error_flag = 0;
	
	public static void main(String[] args) throws IOException
	{
		System.out.println("This is decoder function.");

		//Let we assume that we have a codegraph and also the encoded bits in a string array.
		
		BufferedReader codeGraph_reader = new BufferedReader(new FileReader("CodeGraph.txt"));
		int line_index = 0;
		String line_i;
		String[] line = new String[500];
		line_i = codeGraph_reader.readLine();
		line[line_index] = line_i;
		line_index++;
		while(line_i != null)
		{
			line_i = codeGraph_reader.readLine();
			line[line_index] = line_i;
			line_index++;
		}

		line_index = line_index-1;
		System.out.println("line count is :: " +line_index);
		CodeGraph_original = new String[line_index];
		for(int i=0;i<line_index;i++)
		{
			CodeGraph_original[i] = line[i];
		}

		System.out.println("The CodeGraph_original is thus :: ");
		for(int i=0;i<line_index;i++)
		{
			System.out.println(CodeGraph_original[i]);
		}

		codeGraph_reader.close();

		BufferedReader br = new BufferedReader(new FileReader("graph.txt"));
		int line_index_1 = 0;
		String line_i_1;
		String[] line_1 = new String[500];
		line_i_1 = br.readLine();
		line_1[line_index_1] = line_i_1;
		line_index_1++;
		while(line_i_1 != null)
		{
			line_i_1 = br.readLine();
			line_1[line_index_1] = line_i_1;
			line_index_1++;
		}

		line_index_1 = line_index_1-1;
		System.out.println("line count is :: " +line_index_1);
		CodeGraph = new String[line_index_1];
		for(int i=0;i<line_index_1;i++)
		{
			CodeGraph[i] = line_1[i];
		}

		System.out.println("The CodeGraph is thus :: ");
		for(int i=0;i<line_index_1;i++)
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
		decoder_error();
	}

	
	public static void decoder_error() throws FileNotFoundException, UnsupportedEncodingException
	{
		CodeGraph_length = CodeGraph.length;

		String encoded_items[][] = new String[8][];
		for(int i=0;i<8;i++)											//Tokenize the encoded data into individual items
		{
			String encoded_line = encoded[i];   //get line by line
			encoded_items[i] = encoded_line.split(" ");  //do items[0].length to get length
		}

		
		String CodeGraph_items[][] = new String[CodeGraph_length][];
		for(int i=0;i<CodeGraph_length;i++)								//Tokenize the CodeGraph into individual items
		{
			String CodeGraph_line = CodeGraph[i];
			CodeGraph_items[i] = CodeGraph_line.split(" ");

		}
		

		String xor_result[][] = new String[8][CodeGraph_length];
		for(int k=0;k<8;k++)                                              //denotes line in encoded word here 8 should be replaced by packet size
		{	                                                              //replace 8 by packet size
			for(int i=0;i<CodeGraph_length;i++)								//Tokenize the CodeGraph into individual items
			{
				int len = CodeGraph_items[i].length;
				String result = "0";
				for(int j=0;j<len;j++)
				{
					result = exor(encoded_items[k][Integer.parseInt(CodeGraph_items[i][j])], result);
					//System.out.println("The data is :: " + encoded_items[k][Integer.parseInt(CodeGraph_items[i][j])]);
				}
				xor_result[k][i] = result;                            //we get final result of first exor here
				//System.out.println("result is thus :: " +result);
			}
			
			// here xor_result[k][] is completely filled here we can check for the checksum bits we calculated with the one in encoded bits.
			//and if we find errors, we should flip the bits and do it again.
			int len1 = encoded_items[0].length;
			int yes=0,no=0;
						
				for(int y=0;y<CodeGraph_length;y++)
				{
					if(xor_result[k][y].equals(encoded_items[k][y+((len1-CodeGraph_length))]))
					{
						//System.out.println("No errors found !!");
						yes++;
					}
					else
					{
						//System.out.println("There are errors !!");
						no++;
					}
				}
				
				if(yes<no)
				{
					
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
		for(int i=0;i<8;i++)                                                   //replace 8 by packet size
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
