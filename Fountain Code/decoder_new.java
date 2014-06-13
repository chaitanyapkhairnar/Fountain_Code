import java.io.*;
import java.util.*;


public class decoder_new {

	public static String[] CodeGraph;
	public static String[] encoded;
	public static String[] encoded_packets;
	public static String[] encoded_checksums;
	public static String[] packets_sequence;
	public static String[] checksums_sequence;
	public static int[][] encoded_packets_split;
	public static int[][] encoded_checksums_split;
	public static int encoded_packets_length=0;
	public static int encoded_checksums_length=0;
	public static int CodeGraph_length = 0;
	public static int[] error_flag;
	public static int Seed = 100;
	public static String CodeGraph_final[];

	public static void print(int len, String[] input)
	{
		for(int i=0;i<len;i++)
		{
			System.out.println(input[i]);
		}
	}

	public static void print(int len, int[] input)
	{
		for(int i=0;i<len;i++)
		{
			System.out.println(input[i]);
		}
	}

	public static void print(int len1, int len2, int[][] input)
	{
		for(int i=0;i<len1;i++)
		{
			for(int j=0;j<len2;j++)
			{
				System.out.print(input[i][j]);
				System.out.print(" ");
			}
			System.out.print("\n");
		}
	}

	public static void Reverse_CodeGraph(String[] CodeGraph)
	{
		//CodeGraph[0] contains the connections of 0th element from column 1 to the elements in column2
		//bits[1][] contains the bits of 1st byte from file read.
		//Now we need to take bits from each byte at same indexes and encode using the code graph.

		int codegraph_length = CodeGraph.length;
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

		int codegraph_newlength = CodeGraph_n.length;

		/*System.out.println("The inverted codegraph is thus :: ");
		 * print(codegraph_newlength, CodeGraph_n);
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

		CodeGraph_length = codegraph_newlength;
		System.out.println("The inverted codegraph is thus :: ");
		for(int i=0;i<codegraph_newlength;i++)
		{
			System.out.println(CodeGraph_final[i]);
		}
	}

	public static int countLines(String filename) throws IOException {
		InputStream is = new BufferedInputStream(new FileInputStream(filename));
		try {
			byte[] c = new byte[1024];
			int count = 0;
			int readChars = 0;
			boolean empty = true;
			while ((readChars = is.read(c)) != -1) {
				empty = false;
				for (int i = 0; i < readChars; ++i) {
					if (c[i] == '\n') {
						++count;
					}
				}
			}
			return (count == 0 && !empty) ? 1 : count;
		} finally {
			is.close();
		}
	}

	public static void read_encoded_data(String filename) throws IOException
	{
		int file_length=0;
		file_length = countLines(filename);
		BufferedReader coder = new BufferedReader(new FileReader(filename));
		encoded = new String[file_length+1];
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

		for(int i=0;i<encoded.length-1;i++)
		{
			System.out.println(encoded[i]);
		}
		coder.close();
	}

	public static void split_encoded_data(String[] encoded_data)
	{
		int p=0,q=0,r=0,s=0;
		packets_sequence = new String[encoded_data.length];
		checksums_sequence = new String[encoded_data.length];
		encoded_packets = new String[encoded_data.length];
		encoded_checksums = new String[encoded_data.length];
		System.out.println("Encoded data length is :: "+encoded_data.length);
		for(int i=0;i<encoded_data.length-1;i++)
		{
			//System.out.println("in for loop");
			String line = encoded_data[i];
			String items[];
			items = line.split(" ");
			int flag = Integer.parseInt(items[0]);
			//System.out.println("flag is :: "+flag);
			StringBuilder seq = new StringBuilder();
			for(int j=1;j<7;j++)
			{
				seq.append(items[j]);
			}

			if(flag==1)
			{
				packets_sequence[p] = seq.toString();
				p++;
			}
			else if(flag==0)
			{
				checksums_sequence[q] = seq.toString();
				q++;
			}

			StringBuilder da = new StringBuilder();
			for(int j=7;j<15;j++)                                    //the values are hard coded. Change them
			{
				da.append(items[j]);
				da.append(" ");
			}
			if(flag == 1)
			{
				encoded_packets[r] = da.toString();
				r++;
			}
			else
			{
				encoded_checksums[s] = da.toString();
				s++;
			}
		}


		System.out.println("\n\nThe encoded packets are :: ");
		for(int w=0;w<encoded_packets.length;w++)
		{
			if(encoded_packets[w] != null)
			{
				encoded_packets_length++;
				System.out.println(encoded_packets[w]);
			}
		}

		System.out.println("\n\nThe checksum packets are :: ");
		for(int w=0;w<encoded_checksums.length;w++)
		{
			if(encoded_checksums[w] != null)
			{
				encoded_checksums_length++;
				System.out.println(encoded_checksums[w]);
			}
		}

		System.out.println("\n\nThe packets_sequence are :: ");
		for(int w=0;w<packets_sequence.length;w++)
		{
			if(packets_sequence[w] != null)
				System.out.println(packets_sequence[w]);
		}

		System.out.println("\n\nThe checksums_sequence are :: ");
		for(int w=0;w<checksums_sequence.length;w++)
		{
			if(checksums_sequence[w] != null)
				System.out.println(checksums_sequence[w]);
		}

	}

	public static int binary2decimal(String binary)
	{
		int value=0;
		int binary_int=0;
		binary_int = Integer.parseInt(binary);
		if(binary_int == 0)
		{
			value=0;
		}
		else
		{
			int length = (int)(Math.log10(binary_int)+1);
			int digits[] = new int[length];
			for(int i=0;i<length;i++)
			{
				digits[i] = binary_int%10;
				binary_int = binary_int/10;
			}
			for(int i=0;i<length;i++)
			{
				value+=digits[i]*(Math.pow(2, i));
			}
		}
		return value;
	}


	public static void rearrange_shuffled_encoded_data()
	{
		String temp_data[] = new String[encoded_packets_length];
		String temp_checksum[] = new String[encoded_checksums_length];
		for(int i=0;i<encoded_packets_length;i++)
		{
			int index = binary2decimal(packets_sequence[i]);
			temp_data[index] = encoded_packets[i];
		}
		encoded_packets = temp_data;

		for(int i=0;i<encoded_checksums_length;i++)
		{
			int index = binary2decimal(checksums_sequence[i]);
			temp_checksum[index] = encoded_checksums[i];
		}
		encoded_checksums = temp_checksum;

		System.out.println("The shuffled encoded data is now :: ");
		for(int i=0;i<encoded_packets_length;i++)
		{
			System.out.println(encoded_packets[i]);
		}

		System.out.println("The shuffled checksum data is now :: ");
		for(int i=0;i<encoded_checksums_length;i++)
		{
			System.out.println(encoded_checksums[i]);
		}

	}

	public static int exor(int a, int b)
	{
		int c;
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

	public static int flip(int a)
	{
		if(a==1)
		{
			return 0;
		}
		else if(a==0)
		{
			return 1;
		}
		else
		{
			return -1;
		}
	}

	public static void decode()
	{
		//Split the encoded_packets[i] to get individual bits seperate.
		//Use the codegraph and calculate the checksum
		//check with the encoded_checksum
		//if same then proceed or if error then go into loop.
		//in loop change first bit and calculate and check how many match, continue for all the bits. Max match is kept

		encoded_packets_split = new int[encoded_packets_length][8];
		int[][] encoded_packets_split_copy = new int[encoded_packets_length][8];

		for(int i=0;i<encoded_packets_length;i++)
		{
			int j=0;
			String line = encoded_packets[i];
			StringTokenizer st = new StringTokenizer(line, " ");
			while(st.hasMoreTokens())
			{
				encoded_packets_split[i][j] = Integer.parseInt(st.nextToken());
				j++;
			}
		}
		encoded_packets_split_copy = encoded_packets_split;

		//		System.out.println("Split encoded packets are :: ");
		//		print(encoded_packets_length, 8, encoded_packets_split);

		encoded_checksums_split = new int[encoded_checksums_length][8];
		for(int i=0;i<encoded_checksums_length;i++)
		{
			int j=0;
			String line = encoded_checksums[i];
			StringTokenizer st = new StringTokenizer(line, " ");
			while(st.hasMoreTokens())
			{
				encoded_checksums_split[i][j] = Integer.parseInt(st.nextToken());
				j++;
			}
		}

		//		System.out.println("Split encoded checksums are :: ");
		//		print(encoded_checksums_length, 8, encoded_checksums_split);

		int xor_result;
		int xor_result_array[] = new int[CodeGraph_length];
		error_flag = new int[encoded_packets_length];

		for(int k=0;k<8;k++)
		{
			int error_flag_temp = 0;
			int error_in_bit[] = new int[encoded_checksums_length];
			for(int t=0;t<encoded_checksums_length;t++)
			{
				error_in_bit[t]=0;
			}
			for(int i=0;i<CodeGraph_length;i++)
			{
				xor_result=0;
				String line = CodeGraph_final[i];
				String elements[];
				elements = line.split(" ");
				int ele_len = elements.length;
				for(int s=0;s<ele_len;s++)
				{
					xor_result = exor(encoded_packets_split[Integer.parseInt(elements[s])][k], xor_result);
				}
				//store the xor value in some array
				xor_result_array[i] = xor_result;
			}
			//compare with the checksum_split and if error, flip the first bit and carry on again
			for(int i=0;i<CodeGraph_length;i++)
			{
				if(xor_result_array[i] != encoded_checksums_split[i][k])
				{
					error_flag_temp++;
					//store value of i
					error_in_bit[i] = 1;
				}

			}
			System.out.println("error flag is :: " +error_flag_temp);

			if(error_flag_temp!=0)
			{
				System.out.println("In error loop");

				for(int j=0;j<encoded_packets_length;j++)
				{
					int found_error=0;
					int found_error1=0;
					String error_check[];
					error_check = CodeGraph[j].split(" ");
					for(int w=0;w<error_in_bit.length;w++)
					{
						if(error_in_bit[w] == 1)
						{
							for(int l=0;l<error_check.length;l++)
							{
								if(w==Integer.parseInt(error_check[l]))
								{
									found_error++;
								}
							}
						}
					}

					for(int l=0;l<error_in_bit.length;l++)
					{
						error_in_bit[l]=0;
					}

					error_flag[j] = error_flag_temp;

					error_flag_temp = 0;
					encoded_packets_split_copy[j][k] = flip(encoded_packets_split_copy[j][k]);   //flip the bit

					for(int i=0;i<CodeGraph_length;i++)
					{
						xor_result=0;
						String line = CodeGraph_final[i];
						String elements[];
						elements = line.split(" ");
						int ele_len = elements.length;
						for(int s=0;s<ele_len;s++)
						{
							xor_result = exor(encoded_packets_split_copy[Integer.parseInt(elements[s])][k], xor_result);
						}
						//store the xor value in some array
						xor_result_array[i] = xor_result;
					}
					//compare with the checksum_split of only related bits and if error, flip the first bit and carry on again
					for(int i=0;i<CodeGraph_length;i++)
					{
						if(xor_result_array[i] != encoded_checksums_split[i][k])
						{
							error_flag_temp++;
							error_in_bit[i] = 1;
						}
					}

					for(int w=0;w<error_in_bit.length;w++)
					{
						if(error_in_bit[w] == 1)
						{
							for(int l=0;l<error_check.length;l++)
							{
								if(w==Integer.parseInt(error_check[l]))
								{
									found_error1++;
								}
							}
						}
					}

					/*if(error_flag_temp == 0)
					{
						System.out.println("error solved in "+j+" interation");
						no_error_flag = 1;
						encoded_packets_split = encoded_packets_split_copy;

						System.out.println("Split encoded packets are :: ");
						print(encoded_packets_length, 8, encoded_packets_split);

						break;
					}*/

					if(found_error<found_error1)
					{
						encoded_packets_split_copy[j][k] = flip(encoded_packets_split_copy[j][k]); //flip again and repeat the process
					}

				} //end of for loop

				encoded_packets_split = encoded_packets_split_copy;

				//check for minimum error in error_flag //something fisshy in here
				/*				if(no_error_flag == 0)
				{
					int temp = error_flag[0];
					int index = 0;
					for(int j=1;j<error_flag.length;j++)
					{
						if(error_flag[j]<temp)
						{
							temp = error_flag[j];
							index = j;
						}
					}
					System.out.println("index is :: " +index);

					System.out.println("error flag is :: ");
					print(encoded_packets_length, error_flag);

					//index is the index of error_flag which has minimum errors
					encoded_packets_split[index][k] = flip(encoded_packets_split[index][k]);

					System.out.println("Split encoded packets are :: ");
					print(encoded_packets_length, 8, encoded_packets_split);

				}*/
			}
			System.out.println("out of error loop");

		} //ending of k loop

		System.out.println("Encoded packets split is finally :: ");
		print(encoded_packets_split.length, 8, encoded_packets_split);

	}




	public static void main(String args[]) throws IOException
	{
		CodeGraph = CodeGraphCreate.create_graph(Seed);                                             // Create code graph
		System.out.println("Code Graph is :: ");
		print(CodeGraph.length, CodeGraph);
		Reverse_CodeGraph(CodeGraph);																// Reverse the Code Graph
		read_encoded_data("encoded.txt");															// Read Encoded Data from File
		split_encoded_data(encoded);
		System.out.println("encoded packets length is :: "+encoded_packets_length);
		System.out.println("encoded checksums length is :: "+encoded_checksums_length);
		rearrange_shuffled_encoded_data();
		decode();
		System.out.println("back to main after successfull decode");

	}
}
