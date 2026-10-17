import java.util.Scanner;

public class Main {

    private static final Scanner sc = new Scanner(System.in);
    private static final LoanManagement manager = new LoanManagement();

    private static final String ADMIN_USERNAME = "admin";
    private static final String ADMIN_PASSWORD = "admin123";

    public static void main(String[] args) {

        int choice;

        do {
            System.out.println("\n====================================");
            System.out.println("     BANK LOAN MANAGEMENT SYSTEM");
            System.out.println("====================================");
            System.out.println("1. Customer Registration");
            System.out.println("2. Customer Login");
            System.out.println("3. Admin Login");
            System.out.println("4. Calculate EMI");
            System.out.println("5. Exit");
            System.out.print("Enter your choice: ");

            choice = readInt();

            switch (choice) {

                case 1:
                    registerCustomer();
                    break;

                case 2:
                    customerLogin();
                    break;

                case 3:
                    adminLogin();
                    break;

                case 4:
                    calculateEMI();
                    break;

                case 5:
                    System.out.println("Thank you for using our system!");
                    break;

                default:
                    System.out.println("Invalid choice. Please try again.");
            }

        } while (choice != 5);

        sc.close();
    }

    private static int readInt() {

        while (!sc.hasNextInt()) {
            System.out.print("Please enter a valid number: ");
            sc.next();
        }

        int value = sc.nextInt();
        sc.nextLine();

        return value;
    }

    private static double readDouble() {

        while (!sc.hasNextDouble()) {
            System.out.print("Please enter a valid number: ");
            sc.next();
        }

        double value = sc.nextDouble();
        sc.nextLine();

        return value;
    }

    private static void registerCustomer() {

        System.out.println("\n===== CUSTOMER REGISTRATION =====");

        System.out.print("Enter name: ");
        String name = sc.nextLine().trim();

        System.out.print("Enter phone number: ");
        String phone = sc.nextLine().trim();

        System.out.print("Create password: ");
        String password = sc.nextLine();

        if (name.isEmpty() || phone.isEmpty() || password.isEmpty()) {
            System.out.println("All fields are required.");
            return;
        }

        Customer customer = manager.registerCustomer(
                name, phone, password
        );

        if (customer == null) {
            System.out.println("A customer with this phone number already exists.");
        } else {
            System.out.println("Registration successful!");
            System.out.println("Your Customer ID is: "
                    + customer.getCustomerId());
        }
    }

    private static void customerLogin() {

        System.out.println("\n===== CUSTOMER LOGIN =====");

        System.out.print("Enter phone number: ");
        String phone = sc.nextLine().trim();

        System.out.print("Enter password: ");
        String password = sc.nextLine();

        Customer customer = manager.loginCustomer(phone, password);

        if (customer == null) {
            System.out.println("Invalid phone number or password.");
            return;
        }

        System.out.println("Welcome, " + customer.getName() + "!");
        customerMenu(customer);
    }

    private static void customerMenu(Customer customer) {

        int choice;

        do {
            System.out.println("\n===== CUSTOMER MENU =====");
            System.out.println("1. Apply for Loan");
            System.out.println("2. View My Loans");
            System.out.println("3. Calculate EMI");
            System.out.println("4. Make Loan Repayment");
            System.out.println("5. View Repayment History");
            System.out.println("6. Logout");
            System.out.print("Enter your choice: ");

            choice = readInt();

            switch (choice) {

                case 1:
                    applyForLoan(customer);
                    break;

                case 2:
                    manager.displayCustomerLoans(customer.getCustomerId());
                    break;

                case 3:
                    calculateEMI();
                    break;

                case 4:
                    makeRepayment(customer);
                    break;

                case 5:
                    manager.displayRepaymentHistory(
                            customer.getCustomerId()
                    );
                    break;

                case 6:
                    System.out.println("Logged out successfully.");
                    break;

                default:
                    System.out.println("Invalid choice.");
            }

        } while (choice != 6);
    }

    private static void applyForLoan(Customer customer) {

        System.out.println("\n===== APPLY FOR LOAN =====");
        System.out.println("1. Personal Loan");
        System.out.println("2. Education Loan");
        System.out.println("3. Home Loan");
        System.out.println("4. Vehicle Loan");
        System.out.print("Select loan type: ");

        int type = readInt();

        String loanType;

        switch (type) {
            case 1:
                loanType = "Personal Loan";
                break;
            case 2:
                loanType = "Education Loan";
                break;
            case 3:
                loanType = "Home Loan";
                break;
            case 4:
                loanType = "Vehicle Loan";
                break;
            default:
                System.out.println("Invalid loan type.");
                return;
        }

        System.out.print("Enter loan amount (Rs.): ");
        double amount = readDouble();

        System.out.print("Enter annual interest rate (%): ");
        double rate = readDouble();

        System.out.print("Enter tenure in months: ");
        int tenure = readInt();

        if (amount <= 0 || rate < 0 || tenure <= 0) {
            System.out.println("Please enter valid loan details.");
            return;
        }

        double emi = EMICalculator.calculateEMI(
                amount, rate, tenure
        );

        System.out.printf("Estimated Monthly EMI: Rs. %.2f%n", emi);

        Loan loan = manager.applyForLoan(
                customer.getCustomerId(),
                loanType,
                amount,
                rate,
                tenure
        );

        if (loan != null) {
            System.out.println("Loan application submitted successfully.");
            System.out.println("Your Loan ID is: " + loan.getLoanId());
            System.out.println("Application Status: PENDING");
        } else {
            System.out.println("Unable to submit loan application.");
        }
    }

    private static void calculateEMI() {

        System.out.println("\n===== EMI CALCULATOR =====");

        System.out.print("Enter loan amount (Rs.): ");
        double amount = readDouble();

        System.out.print("Enter annual interest rate (%): ");
        double rate = readDouble();

        System.out.print("Enter tenure in months: ");
        int tenure = readInt();

        if (amount <= 0 || rate < 0 || tenure <= 0) {
            System.out.println("Please enter valid values.");
            return;
        }

        double emi = EMICalculator.calculateEMI(
                amount, rate, tenure
        );

        double totalPayment = EMICalculator.calculateTotalPayment(
                amount, rate, tenure
        );

        double totalInterest = EMICalculator.calculateTotalInterest(
                amount, rate, tenure
        );

        System.out.printf("Monthly EMI: Rs. %.2f%n", emi);
        System.out.printf("Total Repayment: Rs. %.2f%n", totalPayment);
        System.out.printf("Total Interest: Rs. %.2f%n", totalInterest);
    }

    private static void makeRepayment(Customer customer) {

        System.out.println("\n===== LOAN REPAYMENT =====");

        manager.displayCustomerLoans(customer.getCustomerId());

        System.out.print("Enter Loan ID: ");
        int loanId = readInt();

        System.out.print("Enter repayment amount (Rs.): ");
        double payment = readDouble();

        boolean success = manager.makeRepayment(
                customer.getCustomerId(),
                loanId,
                payment
        );

        if (success) {
            System.out.println("Repayment recorded successfully.");
        } else {
            System.out.println(
                    "Repayment failed. Check the loan ID, status, and amount."
            );
        }
    }

    private static void adminLogin() {

        System.out.println("\n===== ADMIN LOGIN =====");

        System.out.print("Enter username: ");
        String username = sc.nextLine();

        System.out.print("Enter password: ");
        String password = sc.nextLine();

        if (username.equals(ADMIN_USERNAME)
                && password.equals(ADMIN_PASSWORD)) {

            System.out.println("Admin login successful.");
            adminMenu();

        } else {
            System.out.println("Invalid admin credentials.");
        }
    }

    private static void adminMenu() {

        int choice;

        do {
            System.out.println("\n===== ADMIN MENU =====");
            System.out.println("1. View All Customers");
            System.out.println("2. View All Loan Applications");
            System.out.println("3. View Loans by Status");
            System.out.println("4. Approve Loan");
            System.out.println("5. Reject Loan");
            System.out.println("6. Logout");
            System.out.print("Enter your choice: ");

            choice = readInt();

            switch (choice) {

                case 1:
                    manager.displayAllCustomers();
                    break;

                case 2:
                    manager.displayAllLoans();
                    break;

                case 3:
                    viewLoansByStatus();
                    break;

                case 4:
                    System.out.print("Enter Loan ID to approve: ");
                    int approveId = readInt();

                    if (manager.approveLoan(approveId)) {
                        System.out.println("Loan approved successfully.");
                    } else {
                        System.out.println(
                                "Unable to approve loan. Check its ID and status."
                        );
                    }
                    break;

                case 5:
                    System.out.print("Enter Loan ID to reject: ");
                    int rejectId = readInt();

                    if (manager.rejectLoan(rejectId)) {
                        System.out.println("Loan rejected successfully.");
                    } else {
                        System.out.println(
                                "Unable to reject loan. Check its ID and status."
                        );
                    }
                    break;

                case 6:
                    System.out.println("Admin logged out.");
                    break;

                default:
                    System.out.println("Invalid choice.");
            }

        } while (choice != 6);
    }

    private static void viewLoansByStatus() {

        System.out.println("\n===== VIEW LOANS BY STATUS =====");
        System.out.println("1. Pending");
        System.out.println("2. Approved");
        System.out.println("3. Rejected");
        System.out.println("4. Closed");
        System.out.print("Select status: ");

        int statusChoice = readInt();
        String status;

        switch (statusChoice) {
            case 1:
                status = "PENDING";
                break;
            case 2:
                status = "APPROVED";
                break;
            case 3:
                status = "REJECTED";
                break;
            case 4:
                status = "CLOSED";
                break;
            default:
                System.out.println("Invalid status selection.");
                return;
        }

        manager.displayLoansByStatus(status);
    }
}