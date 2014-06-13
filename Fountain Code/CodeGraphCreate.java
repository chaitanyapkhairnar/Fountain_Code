import java.util.*;

public class CodeGraphCreate {

	public static int K = 55; //Number of blocks in video file
	public static int N = 0; //Number of packets in each of these K blocks

	public static int c = 3;
	public static int d = 11; //Parameters K should be divisible by d

	//public static int Seed = 100;

	public static String[] create_graph(int Seed)
	{
		//Declaring array variables 'column1' and 'column2' for two columns and 
		//'connections' to maintain the index for connected pairs of the two columns
		int initial_count1 = 20;
		int initial_count2 = 1023;

		int length_of_column = c*K;

		int column1[] = new int[length_of_column];
		int column2[] = new int[length_of_column];
		int connections[] = new int[length_of_column];

		//Initialize the values of all the arrays declared

		for(int i=0;i<length_of_column;i++)
		{
			connections[i] = i;
			column1[i] = initial_count1;
			column2[i] = initial_count2;
			initial_count1=initial_count1+3;
			initial_count2=initial_count2+5;
		}

		/*System.out.println("Connections is ::");
		for(int i=0;i<length_of_column;i++)
		{
			System.out.print(connections[i] + "\t");
		}
		System.out.print("\n");
		System.out.println("Column1 is ::");
		for(int i=0;i<length_of_column;i++)
		{
			System.out.print(column1[i] + "\t");
		}
		System.out.print("\n");
		System.out.println("Column2 is ::");
		for(int i=0;i<length_of_column;i++)
		{
			System.out.print(column2[i] + "\t");
		}
		 */

		// Declare new array for shuffled connections and give it initially the values in connections array

		int connections_shuff[] = new int[length_of_column];

		for(int i=0;i<length_of_column;i++)
		{
			connections_shuff[i] = connections[i];
		}

		//Shuffle the connections_shuff array

		shuffleArray(connections_shuff, Seed);

		/*System.out.println("\n");
		System.out.println("Shuffled Connections is ::");
		for(int i=0;i<length_of_column;i++)
		{
			System.out.print(connections_shuff[i] + "\t");
		}*/

		//Now shuffle column2 in the same way as we shuffled connections

		shuffleArray(column2, Seed);

		/*System.out.println("\n");
		System.out.println("Column2 is ::");
		for(int i=0;i<length_of_column;i++)
		{
			System.out.print(column2[i] + "\t");
		}*/


		// First search the integer value in connections at the same index as it is in column1. Now search the index for this integer value in
		// connection_shuff. Take the integer value at this index in column2_shuff i.e. updated column2.

		//Now we group the nodes in column1

		String column1_grp[] = new String[length_of_column/c];
		int j=0;

		for(int i=0;i<length_of_column;i=i+c)
		{
			StringBuilder stringBuilder = new StringBuilder();
			for(int k=0;k<c;k++)
			{
				stringBuilder.append(Integer.toString(column1[i+k]));
				stringBuilder.append(" ");
			}
			String finalString = stringBuilder.toString();
			column1_grp[j] = finalString;
			j++;
		}

		/*System.out.println("\n");
		System.out.println("Grouped Column1 is ::");
		for(int k=0;k<j;k++)
		{
			System.out.println(column1_grp[k]);
		}*/

		//Now we group column2 in similar fashion

		String column2_grp[] = new String[length_of_column/d];
		j=0;

		for(int i=0;i<length_of_column;i=i+d)
		{
			StringBuilder stringBuilder = new StringBuilder();
			for(int k=0;k<d;k++)
			{
				stringBuilder.append(Integer.toString(column2[i+k]));
				stringBuilder.append(" ");
			}
			String finalString = stringBuilder.toString();
			column2_grp[j] = finalString;
			j++;
		}

		/*System.out.println("\n");
		System.out.println("Grouped Column2 is ::");
		for(int k=0;k<j;k++)
		{
			System.out.println(column2_grp[k]);
		}*/

		//Now we create a new array that has all the connections between these two newly grouped arrays

		String connections_new[] = new String[K];

		// Now we take each element of grouped_column1, tokenize it and for each of the corresponding element, we find its pair in 
		// the tokenized element of grouped_column2.

		for(int k=0;k<K;k++)
		{
			//System.out.println("Inside loop 1 and value of k is " +k);
			int index = 0;
			int pair = 0;
			int flag = 0;
			StringTokenizer st = new StringTokenizer(column1_grp[k], " ");
			StringBuilder stringBuilder2 = new StringBuilder();
			for(int i=0;i<c;i++)
			{
				String token = st.nextToken();  //2            //first token
				//System.out.println("The token value is : " +token);
				int token_int = Integer.parseInt(token);       //convert to integer
				for(int z=0;z<length_of_column;z++)            // search this token in column1 and get its index
				{
					if(column1[z] == token_int)
					{
						index = z;
						//System.out.println("Token found in column1 and the index is : " +index);
						break;
					}
				}
				for(int z=0;z<length_of_column;z++)            //search this index value in shuffled connections and get its index
				{
					if(connections_shuff[z] == index)
					{
						index = z;
						//System.out.println("The value at index in connections_shuffle is " +index);
						break;
					}
				}
				pair = column2[index];        //24             //Get the value at this index which the pair of first token
				//System.out.println("Thus the pair found is " +pair);

				//now find this pair in grouped column2

				for(int z=0;z<((c*K)/d);z++)
				{
					StringTokenizer st2 = new StringTokenizer(column2_grp[z], " ");
					for(int y=0;y<d;y++)
					{
						String token2 = st2.nextToken();
						//System.out.println("The token in coulmn2 group is : " +token2);
						int match = Integer.parseInt(token2);
						if(match == pair)
						{
							//System.out.println("Match found at z = " +z);
							stringBuilder2.append(z);
							stringBuilder2.append(" ");

							connections_new[k] = stringBuilder2.toString();
							//System.out.println("connections_new[k] is now : " +connections_new[k]);
							flag = 1;
							break;
						}
					}
					if(flag == 1)
					{
						flag = 0;
						break;
					}
				}
			}
		}

		/*System.out.print("\n");
		System.out.println("The final new connections list is ::");
		for(int k=0;k<K;k++)
		{
			System.out.println(connections_new[k]);   //This contains the final connections of column1_grp to column2_grp
		}*/

		//We can say connections_new as our code_graph
		return connections_new;
	}

	public static void shuffleArray(int[] ar, int seed)
	{
		//Fisher Yates Method of shuffling
		Random rnd = new Random(seed);
		for (int i = ar.length - 1; i > 0; i--)
		{
			int index = rnd.nextInt(i + 1);
			int a = ar[index];
			ar[index] = ar[i];
			ar[i] = a;
		}
	}

	public static void main(String[] args)
	{
		String Con[] = new String[100];
		Con = create_graph(100);
		int Con_length = Con.length;
		for(int i=0;i<Con_length;i++)
		{
			System.out.println(Con[i]);
		}
	}
}