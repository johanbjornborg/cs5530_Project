package cs5530;

import java.lang.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.io.*;

public class UserInterface {

	/**
	 * @param args
	 */
	public static void displayLoginMenu() {
		System.out.println("        Video Store Management System     ");
		System.out.println("1. Register:");
		System.out.println("2. Login:");
		System.out.println("3. Logout/exit:");
	}

	public static void main(String[] args) {

		Connector con = null;
		String choice;
		String cname;
		String dname;
		String fname, login, pw, pwConf, address, phone, card_no;
		String sql = null;
		int c = 0;
		try {
			// remember to replace the password
			con = new Connector();

			// Variables.setConnection();
			System.out.println("Database connection established");

			BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

			while (true) {
				displayLoginMenu();
				User user = new User(con, con.stmt);
				int retryCount = 0;
				while ((choice = in.readLine()) == null && choice.length() == 0)
					;
				try {
					c = Integer.parseInt(choice);
				} catch (Exception e) {

					continue;
				}
				if (c < 1 | c > 4)
					continue;
				if (c == 1) {

					System.out.println("Register New User:");

					System.out.println("please enter a login:");
					while ((login = in.readLine()) == null && login.length() == 0)
						;
					System.out.println("please enter a Password:");
					while ((pw = in.readLine()) == null && pw.length() == 0)
						;
					System.out.println("please enter your full name:");
					while ((fname = in.readLine()) == null && fname.length() == 0)
						;
					System.out.println("please enter your address:");
					while ((address = in.readLine()) == null && address.length() == 0)
						;
					System.out.println("please enter a phone:");
					while ((phone = in.readLine()) == null && phone.length() == 0)
						;

					System.out.println(user.registerNewUser(fname, login, pw, address, phone));
				} else if (c == 2) {

					String uname;
					String pword;
					// Login here.
					while (retryCount < 3) {
						System.out.println("Username:");
						while ((uname = in.readLine()) == null && uname.length() == 0)
							;
						System.out.println("Password:");
						while ((pword = in.readLine()) == null && pword.length() == 0)
							;
						if (user.loginUser(uname, pword)) {
							Variables.setLogin(user.login);
							userMenu(con, con.stmt, user);
							break;
						} else {
							retryCount++;
						}
					}

				} else {
					System.out.println("Remember to pay us!");
					// con.stmt.close();

					break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("Cannot connect to database server");
		} finally {
			if (con != null) {
				try {
					con.closeConnection();
					System.out.println("Database connection terminated");
				}

				catch (Exception e) { /* ignore close errors */
				}
			}
		}
	}

	/**
	 * 
	 * @param con
	 * @param stmt
	 * @param user
	 */
	public static void userMenu(Connector con, Statement stmt, User user) {
		String choice;
		int mainMenuChoice = 0;
		try {

			BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

			while (true) {
				System.out.println(String.format("\tHello, %s!\t", user.firstName));
				System.out.println("1. Order Movie(s):");
				System.out.println("2. See Order History:");
				System.out.println("3. View/Leave Feedback:");
				System.out.println("4. User Relationships:");
				System.out.println("5. User Reports:");
				System.out.println("--- EMPLOYEE USE ONLY ---");
				System.out.println("6. New Stock:");
				System.out.println("7. Statistics:");
				System.out.println("8. User Awards:");
				System.out.println("9. 2 Degrees Demo:");
				System.out.println("Note: Typing 'exit' at any time will bring you to back to the parent menu.");
				while ((choice = in.readLine()) == null && choice.length() == 0)
					;
				try {
					mainMenuChoice = Integer.parseInt(choice);
				} catch (Exception e) {

					continue;
				}
				if (mainMenuChoice < 1 | mainMenuChoice > 9)
					continue;
				if (mainMenuChoice == 1) {

					String input;
					int quantity;

					// Order a movie
					while (true) {
						QueryOrder order = new QueryOrder(con, stmt, user);
						Date date = new Date();
						int subMenuChoice;
						int isbn = -1;
						System.out.println("1. By ISBN\n2. By Title\n3. Browse Selection\n4. Back to Main Menu");
						while ((choice = in.readLine()) == null && choice.length() == 0)
							;
						try {
							if (choice.equals("exit"))
								break;

							subMenuChoice = Integer.parseInt(choice);
							if (subMenuChoice < 1 || subMenuChoice > 3) {
								continue;
							} else if (subMenuChoice == 4) {
								break;
							}
						} catch (Exception e) {
							continue;
						}
						if (subMenuChoice == 1) {
							while (true) {
								System.out.println("Enter the numeric ISBN:");
								if (isbn == -1) {
									/**
									 * ORDER BY ISBN
									 */
									while ((input = in.readLine()) == null && input.length() == 0)
										;
									try {
										if (input.equals("exit")) {
											break;
										}
										isbn = Integer.parseInt(input);
									} catch (Exception e) {
										System.out.println("Invalid ISBN. Ensure it is entirely numeric.");
										continue;
									}
								}
								System.out.println("Quantity:");
								while ((input = in.readLine()) == null && input.length() == 0)
									;
								try {
									if (input.equals("exit")) {
										break;
									}
									quantity = Integer.parseInt(input);
									if (quantity < 1) {
										System.out.println("Please enter a nonzero, positive integer.");
										continue;
									}
									System.out.println(order.orderVideos(Integer.toString(isbn), "", quantity, date));
									System.out.println(order.buyingSuggestions(isbn));
								} catch (Exception e) {
									System.out.println("Invalid Quantity. Ensure it is entirely numeric.");
									continue;
								}
							}
						} else if (subMenuChoice == 2) {
							/**
							 * ORDER BY TITLE
							 */
							while (true) {
								String title = "";
								System.out.println("Enter the Movie Title:");
								if (title == "") {
									while ((input = in.readLine()) == null && input.length() == 0)
										;
									try {
										if (input.equals("exit")) {
											break;
										} else {
											title = input;
										}
									} catch (Exception e) {
										continue;
									}
								}
								System.out.println("Quantity:");
								while ((input = in.readLine()) == null && input.length() == 0)
									;
								try {
									if (input.equals("exit")) {
										break;
									}
									quantity = Integer.parseInt(input);
									if (quantity < 1) {
										System.out.println("Please enter a nonzero, positive integer.");
										continue;
									}
									System.out.println(order.orderVideos("", title, quantity, date));
									System.out.println(order.buyingSuggestions(isbn));
								} catch (Exception e) {
									System.out.println("Invalid Quantity. Ensure it is entirely numeric.");
									continue;
								}
							}
						} else if (subMenuChoice == 3) {
							/**
							 * BROWSE TITLES
							 */
							QueryVideos qv = new QueryVideos(con, stmt);
							HashMap<String, ArrayList<String>> params = new HashMap<String, ArrayList<String>>();
							params.put("title", new ArrayList<String>());
							params.put("cast", new ArrayList<String>());
							params.put("director", new ArrayList<String>());
							params.put("rating", new ArrayList<String>());
							params.put("genre", new ArrayList<String>());
							params.put("keyword", new ArrayList<String>());

							int key = -1;
							String val = "";
							// SELECT TITLE, ACTOR, RATING, GENRE, ETC.
							System.out.println("Search Parameters. Enter as many as desired.");
							while (true) {
								System.out.println("1: Title\n2: Actor/Actress\n3: Director \n4: Rating \n5: Genre \n6:Keywords \n7: Finish and search");
								while ((input = in.readLine()) == null && input.length() == 0)
									;
								try {
									if (input.equals("exit")) {
										break;
									} else
										key = Integer.parseInt(input);
								} catch (Exception e) {
									System.out.println("Invalid ISBN. Ensure it is entirely numeric.");
									continue;
								}
								System.out.println("Enter Parameter:");
								while ((input = in.readLine()) == null && input.length() == 0)
									;
								try {
									if (input.equals("exit")) {
										break;
									} else
										val = input;
								} catch (Exception e) {
									System.out.println("Invalid ISBN. Ensure it is entirely numeric.");
									continue;
								}
								switch (key) {
								case 1:
									params.get("title").add(val);
									break;
								case 2:
									params.get("cast").add(val);
									break;
								case 3:
									params.get("director").add(val);
									break;
								case 4:
									params.get("rating").add(val);
									break;
								case 5:
									params.get("genre").add(val);
									break;
								case 6:
									params.get("keyword").add(val);
									break;
								case 7:
									int sort;
									System.out.println("Sort By\n1: Year\n2: Avg Feedback Rating\n3: Avg Feedback from Trusted");
									while ((input = in.readLine()) == null && input.length() == 0)
										;
									try {
										if (input.equals("exit")) {
											break;
										} else
											sort = Integer.parseInt(input);
										switch (sort) {
										case 1:
											System.out.println(qv.browseTitles(params, "release_year"));
											break;
										case 2:
											System.out.println(qv.browseTitles(params, "usefulness"));
											break;
										case 3:
											System.out.println(qv.browseTitles(params, "trusted"));
											break;
										default:
											break;
										}
									} catch (Exception e) {
										System.out.println("Invalid ISBN. Ensure it is entirely numeric.");
										continue;
									}
									break;
								}

							}

						}
					}
				} else if (mainMenuChoice == 2) {
					/**
					 * ORDER HISTORIES
					 */
					QueryOrder order = new QueryOrder(con, stmt, user);
					String results;
					System.out.println("Complete Order History for " + user.fullName + ":");
					results = order.getOrderHistory();
					System.out.println(results);
				} else if (mainMenuChoice == 3) {
					/**
					 * FEEDBACKS
					 */
					QueryFeedback feedback = new QueryFeedback(con, stmt, user);
					System.out.println("View/Leave Feedback for movie(s)");
					String input;
					String comments = "";
					int isbn = -1;
					int subMenuChoice;
					while (true) {
						System.out.println("Enter the numeric ISBN:");
						if (isbn == -1) {

							while ((input = in.readLine()) == null && input.length() == 0)
								;
							try {
								if (input.equals("exit")) {
									break;
								}
								isbn = Integer.parseInt(input);
							} catch (Exception e) {
								System.out.println("Invalid ISBN. Ensure it is entirely numeric.");
								isbn = -1;
								continue;
							}
						}
						System.out.println("1. View Feedback\n2. Leave Feedback\n3. Back to Main Menu");
						while ((choice = in.readLine()) == null && choice.length() == 0)
							;
						try {
							if (choice.equals("exit"))
								break;

							subMenuChoice = Integer.parseInt(choice);
							if (subMenuChoice < 1 || subMenuChoice > 3) {
								continue;
							}
						} catch (Exception e) {
							continue;
						}
						if (subMenuChoice == 1) {
							/**
							 * VIEW ALL FEEDBACK
							 */
							String[] attrs = new String[9];
							System.out.println("View All Feedback for Movie: MOVIETITLE");
							boolean exit = false;
							feedback.getFeedback(isbn);

						} else if (subMenuChoice == 2) {
							/**
							 * LEAVE NEW FEEDBACK
							 */
							System.out.println("Leave Feedback:");
							while (true) {
								int score = -1;
								if (score == -1) {
									System.out.println("Score (1-10):");
									while ((input = in.readLine()) == null && input.length() == 0)
										;
									try {
										if (input.equals("exit")) {
											break;
										} else {
											score = Integer.parseInt(input);
											if (score < 1 || score > 10) {
												score = -1;
												System.out.println("Ensure Score is between 1 and 10.");
												continue;
											}
										}
									} catch (Exception e) {
										score = -1;
										System.out.println("Ensure Score is between 1 and 10.");
										continue;
									}
								}

								System.out.println("Detailed feedback. Enter when done.\n>");
								while ((input = in.readLine()) == null && input.length() == 0)
									;
								try {
									if (input.equals("exit")) {
										break;
									} else {
										comments = input;
									}

								} catch (Exception e) {
									System.out.println("Invalid Quantity. Ensure it is entirely numeric.");
									continue;
								}
								feedback.leaveNewFeedback(isbn, comments, score);
								break;
							}
						} else {
							break;
						}
					}
					break;
				} else if (mainMenuChoice == 4) {
					/**
					 * USER RELATIONSHIPS
					 */
					System.out.println("User Relationships");
					System.out.println("1. View\n2. Additional Copies of Existing Movie\n3. Back to Main Menu");

				} else if (mainMenuChoice == 5) {
					/**
					 * USER REPORTS
					 */
					System.out.println("Full User Report for " + user.fullName + ":");
					System.out.println(user.getUserRecord());
					break;
				} else if (mainMenuChoice == 6) {
					System.out.println("1. New Movie\n2. Additional Copies of Existing Movie\n3. Back to Main Menu");

					QueryVideos qv = new QueryVideos(con, stmt);
					int subMenuChoice;
					int isbn = -1;
					int quantity;
					String input;

					while ((choice = in.readLine()) == null && choice.length() == 0)
						;
					try {
						if (choice.equals("exit"))
							break;

						subMenuChoice = Integer.parseInt(choice);
						if (subMenuChoice < 1 || subMenuChoice > 3) {
							continue;
						}
					} catch (Exception e) {
						continue;
					}
					if (subMenuChoice == 1) {
						/**
						 * NEW MOVIE HERE
						 */
						String[] attrs = new String[9];
						System.out.println("Enter each of the following, one line per value:");
						System.out.println("ISBN, Movie Title, year, genre, cast, rating, format, price, quantity in stock:");
						boolean exit = false;
						for (int j = 0; j < 9; j++) {

							while ((input = in.readLine()) == null && input.length() == 0)
								;
							if (input.equals("exit")) {
								exit = true;
								break;
							}
							attrs[j] = input;
						}
						if (exit) {
							break;
						} else {
							qv.newMovie(attrs);
						}

					} else if (subMenuChoice == 2) {
						/**
						 * UPDATE MOVIE
						 */
						while (true) {
							isbn = -1;
							System.out.println("ISBN to update:");
							if (isbn == -1) {
								while ((input = in.readLine()) == null && input.length() == 0)
									;
								try {
									if (input.equals("exit")) {
										break;
									} else {
										isbn = Integer.parseInt(input);
									}
								} catch (Exception e) {
									continue;
								}
							}

							System.out.println("Quantity:");
							while ((input = in.readLine()) == null && input.length() == 0)
								;
							try {
								if (input.equals("exit")) {
									break;
								}
								quantity = Integer.parseInt(input);
								if (quantity < 1) {
									System.out.println("Please enter a nonzero, positive integer.");
									continue;
								}
								qv.updateQuantity(isbn, quantity);
							} catch (Exception e) {
								System.out.println("Invalid Quantity. Ensure it is entirely numeric.");
								continue;
							}
						}
					} else {
						break;
					}

				} else if (mainMenuChoice == 7) {
					System.out.println("Function TBD.");

				} else if (mainMenuChoice == 8) {
					System.out.println("Function TBD.");

				} else if (mainMenuChoice == 9) {
					System.out.println("2 Degrees Demo.\n For the purpose of the assignment, John Goodman (O Brother Where Art Thou) will be compared to Brad Pitt (Oceans Eleven).\n Both of them share a connection through George Clooney.");
					CastAndCrew cc = new CastAndCrew(con, con.stmt);
					System.out.println(cc.twoDegrees("John Goodman", "Brad Pitt"));

				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("Cannot connect to database server");
		} finally {
			if (Variables.getConnection() != null) {
				try {
					Variables.getConnection().close();
					System.out.println("Database connection terminated");
				}

				catch (Exception e) { /* ignore close errors */
				}
			}
		}
	}
}
