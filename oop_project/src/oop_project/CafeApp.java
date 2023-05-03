package oop_project;

import java.io.*;
import java.util.*;

class MenuItem {
	private String name;
	private double price;

	public MenuItem(String name, double price) {
		this.name = name;
		this.price = price;
	}

	public String getName() {
		return name;
	}

	public double getPrice() {
		return price;
	}

	@Override
	public String toString() {
		return name + " ($" + price + ")";
	}
}

class CafeMenu {
	private ArrayList<MenuItem> items;

	public CafeMenu() {
		items = new ArrayList<MenuItem>();
	}

	public void addItem(MenuItem item) {
		items.add(item);
	}

	public ArrayList<MenuItem> getItems() {
		return items;
	}

	public MenuItem getItemByName(String name) throws ItemNotFoundException {
		for (MenuItem item : items) {
			if (item.getName().equals(name)) {
				return item;
			}
		}
		throw new ItemNotFoundException(name);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (MenuItem item : items) {
			sb.append(item.toString()).append("\n");
		}
		return sb.toString();
	}
}

class ItemNotFoundException extends Exception {
	private static final long serialVersionUID = 1L;

	public ItemNotFoundException(String name) {
		super(name + " not found in the menu.");
	}
}

class Order {
	private ArrayList<MenuItem> items;

	public Order() {
		items = new ArrayList<MenuItem>();
	}

	public void addItem(MenuItem item) {
		items.add(item);
	}

	public double getTotalPrice() {
		double totalPrice = 0;
		for (MenuItem item : items) {
			totalPrice += item.getPrice();
		}
		return totalPrice;
	}

	public String getOrderSummary() {
		StringBuilder sb = new StringBuilder();
		for (MenuItem item : items) {
			sb.append(item.getName()).append("\t\t$").append(item.getPrice()).append("\n");
		}
		sb.append("Total price:\t$").append(getTotalPrice());
		return sb.toString();
	}
}

class Cafe {
	private CafeMenu menu;
	private ArrayList<Order> orderHistory;

	public Cafe() {
		menu = new CafeMenu();
		orderHistory = new ArrayList<Order>();
	}

	public void loadMenuFromFile(String filename) throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader(filename));
		String line;
		while ((line = reader.readLine()) != null) {
			String[] parts = line.split(",");
			String name = parts[0];
			double price = Double.parseDouble(parts[1]);
			MenuItem item = new MenuItem(name, price);
			menu.addItem(item);
		}
		reader.close();
	}

	public CafeMenu getMenu() {
		return menu;
	}

	public Order createOrder(String[] itemNames) throws ItemNotFoundException {
		Order order = new Order();
		for (String itemName : itemNames) {
			MenuItem item = menu.getItemByName(itemName);
			order.addItem(item);
		}
		orderHistory.add(order);
		return order;
	}

	public void viewOrderHistory() {
		if (orderHistory.size() == 0) {
			System.out.println("No order history found.");
		} else {
			System.out.println("Order history:");
			for (int i = 0; i < orderHistory.size(); i++) {
				System.out.println("Order " + (i + 1) + ":");
				System.out.println(orderHistory.get(i).getOrderSummary());
			}
		}
	}
}

public class CafeApp {
	public static void main(String[] args) {
		Cafe cafe = new Cafe();
		try {
			cafe.loadMenuFromFile("menu.txt");
		} catch (IOException e) {
			System.out.println("Failed to load menu from file.");
			System.exit(1);
		}

		Scanner scanner = new Scanner(System.in);
		int choice;
		do {
			System.out.println("Welcome to CafeApp!");
			System.out.println("--------------------");
			System.out.println("1. View Menu");
			System.out.println("2. Create Order");
			System.out.println("3. View Order History");
			System.out.println("4. Exit");
			System.out.print("Enter your choice: ");
			choice = scanner.nextInt();
			scanner.nextLine();

			switch (choice) {
			case 1:
				System.out.println("Menu:");
				System.out.println(cafe.getMenu().toString());
				break;
			case 2:
				System.out.println("Menu:");
				System.out.println(cafe.getMenu().toString());
				ArrayList<String> itemNames = new ArrayList<String>();
				while (true) {
					System.out.print("Enter item number (Type 0 to finish order): ");
					int itemNumber = scanner.nextInt();
					scanner.nextLine();
					if (itemNumber == 0) {
						break;
					}
					try {
						MenuItem item = cafe.getMenu().getItems().get(itemNumber - 1);
						itemNames.add(item.getName());
					} catch (IndexOutOfBoundsException e) {
						System.out.println("Invalid item number.");
					}
				}
				try {
					Order order = cafe.createOrder(itemNames.toArray(new String[itemNames.size()]));
					System.out.println("Order created successfully.");
					System.out.println(order.getOrderSummary());
					System.out.print("Confirm order (y/n)? ");
					String confirm = scanner.nextLine();
					if (confirm.equalsIgnoreCase("y")) {
						System.out.println("Order confirmed.");
					} else {
						System.out.println("Order cancelled.");
					}
				} catch (ItemNotFoundException e) {
					System.out.println(e.getMessage());
				}
				break;
			case 3:
				cafe.viewOrderHistory();
				break;
			case 4:
				System.out.println("Thank you for using CafeApp!");
				break;
			default:
				System.out.println("Invalid choice.");
				break;
			}
			System.out.println();
		} while (choice != 4);
		scanner.close();
	}
}