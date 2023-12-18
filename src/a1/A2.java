package a1;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class A2 {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		boolean flag = true;
		Scanner scanner = null;
		Data tours = null;
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		String filepath = null;
		while (flag) {
			scanner = new Scanner(System.in);
			System.out.println("What is the name of the event file?");
			filepath = scanner.nextLine();
			File file = new File(filepath);
			try {
				Scanner sc = new Scanner(file);
				String l = "";
				while (sc.hasNext()) {
					l += sc.nextLine();
				}
				sc.close();
				tours = gson.fromJson(l, Data.class);
			} catch (IOException e) {
				System.out.println("The file " + filepath + " cannot be found.");
				continue;
			}
			boolean format = true;
			for (Event t : tours.data) {
				if (t.localDate == null || t.name == null || t.tour == null || t.venue == null
						|| !dateValid(t.localDate)) {
					System.out.println("The file " + filepath + " is not formatted properly.");
					format = false;
					break;
				}
			}
			if (format == true) {
				flag = false;
			}
		}

		System.out.println("The file has been properly read");

		boolean run = true;
		int choice = 0;
		while (run) {
			System.out.println("	1) Display all events");
			System.out.println("	2) Search for an event (by tour)");
			System.out.println("	3) Search for all events on a venue");
			System.out.println("	4) Add a new event");
			System.out.println("	5) Remove an event");
			System.out.println("	6) Sort events");
			System.out.println("	7) Exit");
			System.out.println("What would you like to do?");
			boolean check = true;
			while (check) {
				try {
					choice = Integer.parseInt(scanner.nextLine());
				} catch (NumberFormatException e) {
					System.out.println("That is not a valid option.");
					continue; 
				}
				if (choice > 7 || choice < 1) {
					System.out.println("That is not a valid option."); 
				} else {
					check = false;
				}
			}
			if (choice == 1) {
				for (int i = 0; i < tours.data.size(); i++) {
					Event temp = tours.data.get(i);
					System.out.println(
							temp.name + ", " + temp.tour + ", on " + temp.localDate + ", held at " + temp.venue + "\n");
				}
			} else if (choice == 2) {
				System.out.println("What is the name of the tour you would like to search for?");
				String search = scanner.nextLine();
				int counter = 0; 
				for (Event t : tours.data) {
					if (t.getTour().equalsIgnoreCase(search)) {
						System.out.println(t.name + ", " + t.tour + ", on " + t.localDate + ", held at " + t.venue);
						counter++; 
					}
				}
				if (counter == 0) {
					System.out.println("There are no tours for this specific search.");
				}
			} else if (choice == 3) {
				System.out.println("What venue would you like to search for?");
				String search = scanner.nextLine();
				List<String> venues = new ArrayList<String>();
				List<String> names = new ArrayList<String>();
				int counter = 0; 
				for (Event t : tours.data) {
					if (t.getVenue().equalsIgnoreCase(search)) {
						venues.add(t.getVenue());
						names.add(t.getName()); 
						counter++; 
					}
				}
				
				if (counter == 0) {
					System.out.println("There are no tours for this specific search.");
				} else {
					for (int i = 0; i < venues.size(); i ++) {
						System.out.print(names.get(i) + ", ");
					}
					System.out.print("found on the " + search + " venue.");
				}
				System.out.println();
			} else if (choice == 4) {
				System.out.println("What is the name of the event you would like to add?");
				String name = scanner.nextLine();
				System.out.println("What is the tour name for " + name + "?");
				String tour = scanner.nextLine();
				System.out.println("What is the date of " + name + "'s event?");
				String date = scanner.nextLine();
				while (!dateValid(date)) {
					System.out.println("What is the date of " + name + "'s event?");
					date = scanner.nextLine();
				}
				System.out.println("What is the venue where " + name + " has an event?");
				String venue = scanner.nextLine();
				Event t = new Event();
				t.setName(name);
				t.setTour(tour);
				t.setLocalDate(date);
				t.setVenue(venue);
				tours.getData().add(t);
				System.out.println("There is a new entry now for " + t.getName() + " ," + t.getTour() + ", on "
						+ t.getLocalDate() + ", held at " + t.getVenue());

			} else if (choice == 5) {
				System.out.println("Which event would you like to remove?");
				int counter = 1;
				for (Event t : tours.data) {
					System.out.println("	" + counter + ") " + t.name);
					counter++;
				}
				int event = Integer.parseInt(scanner.nextLine());
				String name = tours.data.get(event - 1).getName();
				tours.getData().remove(event - 1);
				System.out.println(name + " is now removed.");

			} else if (choice == 6) {
				System.out.println("How would you like to sort by?");
				System.out.println("1) A to Z");
				System.out.println("2) Z to A");
				int option = Integer.parseInt(scanner.nextLine());
				Collections.sort(tours.getData(), new Comparator<Event>() {
					public int compare(Event t1, Event t2) {
						int val = t1.getName().compareTo(t2.getName());
						if (option == 1) {
							return val;
						} else {
							return val * -1;
						}
					}
				});
				
				if (option == 1) {
					System.out.println("Your events are now sorted from A to Z.");
				} else {
					System.out.println("Your events are now sorted from Z to A.");
				}

			} else if (choice == 7) {
				System.out.println("1) Yes");
				System.out.println("2) No");
				System.out.println("Would you like to save your edits?");
				int save = Integer.parseInt(scanner.nextLine());
				if (save == 1) {
					try {
						FileWriter writer = new FileWriter(filepath);
						writer.write(gson.toJson(tours));
						writer.close();
					} catch (IOException e) {
						System.out.println("File not saved properly.");
					}
				}
				System.out.println("Thank you for using my program!");
				run = false;
				scanner.close();
			}
		}
	}

	private static boolean dateValid(String date) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		try {
			format.parse(date);
		} catch (ParseException e) {
			return false;
		}
		return true;
	}
}

class Event {

	public String name;
	public String tour;
	public String venue;
	public String localDate;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTour() {
		return tour;
	}

	public void setTour(String tour) {
		this.tour = tour;
	}

	public String getVenue() {
		return venue;
	}

	public void setVenue(String venue) {
		this.venue = venue;
	}

	public String getLocalDate() {
		return localDate;
	}

	public void setLocalDate(String localDate) {
		this.localDate = localDate;
	}
}

class Data {
	List<Event> data;

	public List<Event> getData() {
		return data;
	}

	public void setData(List<Event> data) {
		this.data = data;
	}
}