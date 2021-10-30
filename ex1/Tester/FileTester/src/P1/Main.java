package P1;

import java.io.FileReader;
import java.io.BufferedReader;

public class Main {
public static void main(String[] args) {
		
		String outputFilename, expectedOutputFilename;
		FileReader outputFile_reader, expectedOutputFile_reader;
		
		try {
			outputFilename = args[0];
			expectedOutputFilename = args[1];
		}
		catch (Exception e) {
			
			System.out.println("Two file names required. The output file and the expected output file");
			
			e.printStackTrace();
			
			return;
		}
		
		try {
			outputFile_reader = new FileReader(outputFilename);
			expectedOutputFile_reader = new FileReader(expectedOutputFilename);	
		}
		catch (Exception e) {
			
			System.out.println("A file was not found");
			
			e.printStackTrace();
			
			return;
		}
		
		try{ 
			BufferedReader output = new BufferedReader(outputFile_reader);
			BufferedReader expected = new BufferedReader(expectedOutputFile_reader);
			
			String line, expLine;
			
			int i = 0, errors = 0;
			
			while ((line = output.readLine()) != null) {
				expLine = expected.readLine();
				
				if (!(line.equals(expLine))) {
					System.out.println("Error at line " + i);
					System.out.println("Got: " + line + ",   Expected: " + expLine);
					
					errors ++;
				}
				
				i++;
			}
			
			if (errors == 0) {
				System.out.println("PASSED!");
			}
			
			output.close();
			expected.close();
			
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
	}
}
