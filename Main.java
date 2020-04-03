import java.io.*;
import java.util.*;
import java.util.ArrayList;
//A Node for the hashMap
class Node {
	private String key;
	private int num;
	Node next;
	Node prev;
	Node(String key, int num) {
		this.key = key;
		this.num = num;
	}
	public String getKey() {
		return this.key;
	}
	public int getNum() {
		return this.num;
	}
	public String toString() {
		return "There are " + num + " movies with the genre of " + key;
	} 
}
//Custom HashMap
class HashMap {
	Node first;
	Node last;
	//put method for the HashMap
	public void put(String key, int num) {
		Node newNode = new Node(key, num);		
		if (first == null) {
			first = newNode;
		}
		else {
			last.next = newNode;
			newNode.prev = last;
    	}
    	last = newNode;
	}
	//get method for the HashMap
	public int get(String key) {
		Node focusNode = first;
		int num = 0;
		while(focusNode != null) {
			if (focusNode.getKey().equals(key))
				num = focusNode.getNum();
			focusNode = focusNode.next;
    	}
    	return num;
	}
	//Initiates the printing sequence in accending order
	//determines the max value and min value and uses the rec method to print the rest recursively
	public void printHashMap(PrintStream output) {
		Node focusNode = first;
		Node max = first;
		Node min = first;
		while(focusNode != null) {
			if (focusNode.getNum() > max.getNum())
				max = focusNode;
			if (focusNode.getNum() < min.getNum())
				min = focusNode;
			focusNode = focusNode.next;

    	}
    	output.println(max);
    	rec(max, min, output);
    }
    private void rec(Node max, Node min, PrintStream output) {
    	Node focusNode = first;
    	Node temp = max;
    	max = min;
    	while(focusNode != null) {
    		if (focusNode.getNum() < temp.getNum() && focusNode.getNum() > max.getNum())
				max = focusNode;
			focusNode = focusNode.next;
		}
		output.println(max);
		if (max.getNum() != min.getNum()) {
			rec(max, min, output);
		}
	}
}
public class Main {
	//for the first half of the assignment genresToHashMap is meant to determine the
	//what all the unique genres are in the csv file and how many are there of each
	//then it takes the genres and stores them in our custom HashMap as keys and
	//the number occurrences as the values
	public static void genresToHashMap(HashMap newMap) throws IOException {
		//Scanner sc traverses through the csv file line by line
		BufferedReader br = new BufferedReader(new FileReader("movies.csv"));
		Scanner sc = new Scanner(br);
		//a1 is used for storing genres
		ArrayList<String> a1 = new ArrayList<String>();
		//a2 is used for storing the number of occurrences
		ArrayList<Integer> a2 = new ArrayList<Integer>();
		while (sc.hasNext()) {
			//each line in the csv file become a string for us to work with
			String temp = sc.nextLine();
	  		String[] genres;
			//some movies don't have genres listed thus this part of the algorithm checks
			//if it has genres and sets String[] genres as null
			if (temp.substring(temp.lastIndexOf(",")).contains("no genres listed")) {
				genres = null;
			}
			//if a movie does have genres then it takes the substring and splits
			//the string by "|" and turns it into a string array
			else {
				String genre = temp.substring(temp.lastIndexOf(",")+1);
      			genres = genre.split("\\|");
			}
			//takes the isolated genres and begins storing them in the ArrayList
			//if the genre doesn't exist in the ArrayList then it adds it to the list
			//and adds 1 to the ArrayList storing the number of occurrences in the same index
			if (genres != null) {
				for (String x : genres) {
					if (!a1.contains(x)) {
						a1.add(x);
						a2.add(1);
					}
					//if the genre already exits in the ArrayList then it just adds 1 to the 
					//ArrayList storing the number of occurrences in the same index
					else {
						int index = a1.indexOf(x);
						a2.set(index, a2.get(index)+1);
					}
				}
	  		}
		}
		//put the keys and the vlaues in the HashMap
		for (int i=0; i<a1.size(); i++) {
			newMap.put(a1.get(i), a2.get(i));
		}
	}
	//yearlyReleases is for the second half of the assignment is mean to
	//count how many movies came out for each genre each year
	public static void yearlyReleases(PrintStream output) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader("movies.csv"));
		Scanner sc = new Scanner(br);
		//years is used for storing all the unique years
		ArrayList<Integer> years = new ArrayList<Integer>();
		//genresPerYear is used for storing the ArrayList (listOfGenres) that contain the genres for the
		//specific year in the years ArrayList in the same index
		ArrayList<ArrayList<String>> genresPerYear = new ArrayList<ArrayList<String>>();
		//numOfGenresPerYear is used ofr storing the ArrayList (numOfGenres) that contain the number of occurrences
		//for each genre for each year in the same index
		ArrayList<ArrayList<Integer>> numOfGenresPerYear = new ArrayList<ArrayList<Integer>>();
		while (sc.hasNext()) {
			//listOfGenres  is used for stroing the unique genres for a specific year
			ArrayList<String> listOfGenres = new ArrayList<String>();
			//numOfGenres is used for stroing the number of occurrences for a specifc genre in the same index
			ArrayList<Integer> numOfGenres = new ArrayList<Integer>();
			String temp = sc.nextLine();
			int release = 0;
			String yearSubstring = "";
			//since some movies don't have a release year then this part of the algorithm checks
			//for the existence of a ")" since all dates are written between parentheses
			if ((temp.substring(temp.lastIndexOf(",")-3, temp.lastIndexOf(","))).contains(")")) {
				yearSubstring = temp.substring(temp.lastIndexOf("(")+1, temp.lastIndexOf(")"));
      			//some movie title are between "" thus this part of the algorithm checks for
      			//them and tries to isolate the release year substring between them
      			if (temp.contains("\"")) {
      				yearSubstring = temp.substring(0, temp.lastIndexOf(",")-1);
      				yearSubstring = yearSubstring.substring(yearSubstring.lastIndexOf("(")+1, yearSubstring.lastIndexOf(")"));
      			}
      			else {
      				yearSubstring = temp.substring(0, temp.lastIndexOf(","));
      				yearSubstring = yearSubstring.substring(yearSubstring.lastIndexOf("(")+1, yearSubstring.lastIndexOf(")"));
      			}
      			//takes the year Substring and turns it to an int
      			release = Integer.valueOf(yearSubstring);
      			//temporarily stores the genres in this array
      			String[] genres;
      			if (temp.substring(temp.lastIndexOf(",")).contains("no genres listed")) {
      				genres = null;
      			}
      			//if a movie does have genres then it takes the substring and splits
				//the string by "|" and turns it into a string array
				else {
					String genre = temp.substring(temp.lastIndexOf(",")+1);
      				genres = genre.split("\\|");
				}
				//stores the genres in listOfGenres
				//and stores the number of Occurences in numOfGenres
				if (genres != null) {
					for (String x : genres) {
						if (!listOfGenres.contains(x)) {
							listOfGenres.add(x);
							numOfGenres.add(1);
						}
						else {
							int i = listOfGenres.indexOf(x);
							numOfGenres.set(i, numOfGenres.get(i)+1);
						}
					}
				}
				//this part of the algorithim is the most complicated to explain
				//essentially what it does is determine if the reakese year has been added to the ArrayList or not
				//if  not then it adds the realese year and the coresponding genres and number of occurences in their respecive ArrayLists
      			if (!(years.contains(release))) {
      				years.add(release);
      				genresPerYear.add(listOfGenres);
      				numOfGenresPerYear.add(numOfGenres);
      			}
      			//if the year has been added to the list then it checks the ArrayList within genresPerYear to see if it
      			//needs to add new unique genres to the coresponding ArrayList within the same index as the year
      			//if not then it just adds 1 to the number of occurences for each non-unique genre that coresponds to the year
      			else {
      				int index = years.indexOf(release);
      				listOfGenres = genresPerYear.get(index);
      				numOfGenres = numOfGenresPerYear.get(index);
      				if (genres != null) {
      					for (String x : genres) {
							if (!listOfGenres.contains(x)) {
								listOfGenres.add(x);
								numOfGenres.add(1);
							}
							else {
								int indx = listOfGenres.indexOf(x);
								numOfGenres.set(indx, numOfGenres.get(indx)+1);
							}
						}
					}
      				genresPerYear.set(index, listOfGenres);
      				numOfGenresPerYear.set(index, numOfGenres);
      			}
      		}
      	}
      	//simple or not so simply prints all of the years and thier coresponding genres and number of occurences
      	for (int i=0; i<years.size(); i++) {
      		output.print("In " + years.get(i) + " there were ");
      		for(int j=0; j<genresPerYear.get(i).size()-1; j++) {
      			if ((numOfGenresPerYear.get(i)).get(j) == 1)
      				output.print((numOfGenresPerYear.get(i)).get(j) + " movie with the genre of " + (genresPerYear.get(i)).get(j) + ", ");
      			else if ((numOfGenresPerYear.get(i)).get(j) != 1 && j != genresPerYear.get(i).size()-2)
      				output.print((numOfGenresPerYear.get(i)).get(j) + " movies with the genre of " + (genresPerYear.get(i)).get(j) + ", ");
      			else if (j == genresPerYear.get(i).size()-2 && (numOfGenresPerYear.get(i)).get(j) == 1)
      				output.print("and " + (numOfGenresPerYear.get(i)).get(j+1) + " movies with the genre of " + (genresPerYear.get(i)).get(j+1) + ".");
      			else
      				output.print("and " + (numOfGenresPerYear.get(i)).get(j+1) + " movie with the genre of " + (genresPerYear.get(i)).get(j+1) + ".");
      		}
      		output.println();
		}
	}
	public static void main(String[] args) throws IOException {
		PrintStream output = new PrintStream(new FileOutputStream("output.txt"));
		//creates an object of the HashMap
		HashMap genreMap = new HashMap();
		//takes the genres and occurences and stores them in the HashMap
		genresToHashMap(genreMap);		
		//prints the HashMap in accending order
		genreMap.printHashMap(output);
		//just to leave a space between the two parts in the output file
		output.println();
		//prints the number of movies that came out for each genre for each year
		yearlyReleases(output);
	}
}
