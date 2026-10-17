import java.io.*;
import java.util.ArrayList;


public class LoanManagement {

    private final ArrayList<Customer> customers = new ArrayList<>();
    private final ArrayList<Loan> loans = new ArrayList<>();

    private final String dataDirectory = "data";
    private final String customerFile = "data/customers.txt";
    private final String loanFile = "data/loans.txt";
    private final String repaymentFile = "data/repayments.txt";

    private int nextCustomerId = 1001;
    private int nextLoanId = 5001;

    public LoanManagement() {
        createDataDirectory();
        loadCustomers();
        loadLoans();
    }

    private void createDataDirectory() {
        File directory = new File(dataDirectory);

        if (!directory.exists() && !directory.mkdirs()) {
            System.out.println("Warning: Could not create data directory.");
        }
    }

    public Customer registerCustomer(
            String name, String phone, String password) {

        for (Customer customer : customers) {
            if (customer.getPhone().equals(phone)) {
                return null;
            }
        }

        Customer customer = new Customer(
                nextCustomerId++, name, phone, password
        );

        customers.add(customer);
        saveCustomers();

        return customer;
    }

    public Customer loginCustomer(String phone, String password) {

        for (Customer customer : customers) {
            if (customer.getPhone().equals(phone)
                    && customer.checkPassword(password)) {
                return customer;
            }
        }

        return null;
    }

    public Loan applyForLoan(
            int customerId,
            String loanType,
            double amount,
            double interestRate,
            int tenureMonths) {

        if (findCustomerById(customerId) == null) {
            return null;
        }

        if (amount <= 0 || interestRate < 0 || tenureMonths <= 0) {
            return null;
        }

        Loan loan = new Loan(
                nextLoanId++, customerId, loanType,
                amount, interestRate, tenureMonths
        );

        loans.add(loan);
        saveLoans();

        return loan;
    }

    public boolean approveLoan(int loanId) {

        Loan loan = findLoanById(loanId);

        if (loan == null || !loan.getStatus().equals("PENDING")) {
            return false;
        }

        loan.approveLoan();
        saveLoans();

        return true;
    }

    public boolean rejectLoan(int loanId) {

        Loan loan = findLoanById(loanId);

        if (loan == null || !loan.getStatus().equals("PENDING")) {
            return false;
        }

        loan.rejectLoan();
        saveLoans();

        return true;
    }

    public boolean makeRepayment(
            int customerId, int loanId, double payment) {

        Loan loan = findLoanById(loanId);

        if (loan == null
                || loan.getCustomerId() != customerId
                || !loan.getStatus().equals("APPROVED")
                || payment <= 0
                || payment > loan.getOutstandingBalance()) {
            return false;
        }

        loan.makeRepayment(payment);
        saveLoans();

        String record = loanId + "|" + customerId + "|"
                + payment + "|" + System.currentTimeMillis();

        try (BufferedWriter writer = new BufferedWriter(
                new FileWriter(repaymentFile, true))) {

            writer.write(record);
            writer.newLine();

        } catch (IOException e) {
            System.out.println("Warning: Could not save repayment record.");
        }

        return true;
    }

    public Customer findCustomerById(int customerId) {

        for (Customer customer : customers) {
            if (customer.getCustomerId() == customerId) {
                return customer;
            }
        }

        return null;
    }

    public Loan findLoanById(int loanId) {

        for (Loan loan : loans) {
            if (loan.getLoanId() == loanId) {
                return loan;
            }
        }

        return null;
    }

    public void displayCustomerLoans(int customerId) {

        boolean found = false;

        for (Loan loan : loans) {
            if (loan.getCustomerId() == customerId) {
                loan.displayLoanDetails();
                found = true;
            }
        }

        if (!found) {
            System.out.println("No loan applications found.");
        }
    }

    public void displayAllCustomers() {

        if (customers.isEmpty()) {
            System.out.println("No customers registered.");
            return;
        }

        for (Customer customer : customers) {
            customer.displayCustomerDetails();
            System.out.println();
        }
    }

    public void displayAllLoans() {

        if (loans.isEmpty()) {
            System.out.println("No loan applications found.");
            return;
        }

        for (Loan loan : loans) {
            loan.displayLoanDetails();
        }
    }

    public void displayLoansByStatus(String status) {

        boolean found = false;

        for (Loan loan : loans) {
            if (loan.getStatus().equals(status)) {
                loan.displayLoanDetails();
                found = true;
            }
        }

        if (!found) {
            System.out.println("No " + status.toLowerCase()
                    + " loan applications found.");
        }
    }

    public void displayRepaymentHistory(int customerId) {

        File file = new File(repaymentFile);
        boolean found = false;

        if (!file.exists()) {
            System.out.println("No repayment records found.");
            return;
        }

        try (BufferedReader reader = new BufferedReader(
                new FileReader(file))) {

            String line;

            while ((line = reader.readLine()) != null) {

                String[] parts = line.split("\\|");

                if (parts.length >= 4
                        && Integer.parseInt(parts[1]) == customerId) {

                    System.out.println("Loan ID: " + parts[0]);
                    System.out.println("Customer ID: " + parts[1]);

                    System.out.printf(
                            "Payment: Rs. %.2f%n",
                            Double.parseDouble(parts[2])
                    );

                    System.out.println("Recorded At: " + parts[3]);
                    System.out.println("-----------------------------");

                    found = true;
                }
            }

        } catch (IOException | NumberFormatException e) {
            System.out.println("Could not read repayment history.");
        }

        if (!found) {
            System.out.println("No repayment records found.");
        }
    }

    private void saveCustomers() {

        try (BufferedWriter writer = new BufferedWriter(
                new FileWriter(customerFile))) {

            for (Customer customer : customers) {
                writer.write(customer.toFileString());
                writer.newLine();
            }

        } catch (IOException e) {
            System.out.println("Error saving customers.");
        }
    }

    private void loadCustomers() {

        File file = new File(customerFile);

        if (!file.exists()) {
            return;
        }

        try (BufferedReader reader = new BufferedReader(
                new FileReader(file))) {

            String line;

            while ((line = reader.readLine()) != null) {

                String[] parts = line.split("\\|", -1);

                if (parts.length != 4) {
                    continue;
                }

                try {
                    int id = Integer.parseInt(parts[0]);

                    Customer customer = new Customer(
                            id, parts[1], parts[2], parts[3]
                    );

                    customers.add(customer);
                    nextCustomerId = Math.max(nextCustomerId, id + 1);

                } catch (NumberFormatException e) {
                    System.out.println("Skipping invalid customer record.");
                }
            }

        } catch (IOException e) {
            System.out.println("Error loading customers.");
        }
    }

    private void saveLoans() {

        try (BufferedWriter writer = new BufferedWriter(
                new FileWriter(loanFile))) {

            for (Loan loan : loans) {
                writer.write(loan.toFileString());
                writer.newLine();
            }

        } catch (IOException e) {
            System.out.println("Error saving loans.");
        }
    }

    private void loadLoans() {

        File file = new File(loanFile);

        if (!file.exists()) {
            return;
        }

        try (BufferedReader reader = new BufferedReader(
                new FileReader(file))) {

            String line;

            while ((line = reader.readLine()) != null) {

                String[] parts = line.split("\\|", -1);

                if (parts.length != 8) {
                    continue;
                }

                try {
                    int loanId = Integer.parseInt(parts[0]);
                    int customerId = Integer.parseInt(parts[1]);

                    Loan loan = new Loan(
                            loanId,
                            customerId,
                            parts[2],
                            Double.parseDouble(parts[3]),
                            Double.parseDouble(parts[4]),
                            Integer.parseInt(parts[5])
                    );

                    String status = parts[6];
                    double balance = Double.parseDouble(parts[7]);

                    if (status.equals("APPROVED")) {
                        loan.approveLoan();
                        loan.makeRepayment(
                                Math.max(0, loan.getOutstandingBalance() - balance)
                        );
                    } else if (status.equals("REJECTED")) {
                        loan.rejectLoan();
                    } else if (status.equals("CLOSED")) {
                        loan.approveLoan();
                        loan.makeRepayment(loan.getOutstandingBalance());
                    }

                    loans.add(loan);
                    nextLoanId = Math.max(nextLoanId, loanId + 1);

                } catch (NumberFormatException e) {
                    System.out.println("Skipping invalid loan record.");
                }
            }

        } catch (IOException e) {
            System.out.println("Error loading loans.");
        }
    }
}