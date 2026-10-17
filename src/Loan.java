public class Loan {

    private int loanId;
    private int customerId;
    private String loanType;
    private double amount;
    private double annualInterestRate;
    private int tenureMonths;
    private String status;
    private double outstandingBalance;

    public Loan(int loanId, int customerId, String loanType,
                double amount, double annualInterestRate,
                int tenureMonths) {

        this.loanId = loanId;
        this.customerId = customerId;
        this.loanType = loanType;
        this.amount = amount;
        this.annualInterestRate = annualInterestRate;
        this.tenureMonths = tenureMonths;
        this.status = "PENDING";
        this.outstandingBalance = 0;
    }

    public int getLoanId() {
        return loanId;
    }

    public int getCustomerId() {
        return customerId;
    }

    public String getLoanType() {
        return loanType;
    }

    public double getAmount() {
        return amount;
    }

    public double getAnnualInterestRate() {
        return annualInterestRate;
    }

    public int getTenureMonths() {
        return tenureMonths;
    }

    public String getStatus() {
        return status;
    }

    public double getOutstandingBalance() {
        return outstandingBalance;
    }

    public void approveLoan() {
        status = "APPROVED";

        outstandingBalance = EMICalculator.calculateEMI(
                amount, annualInterestRate, tenureMonths
        ) * tenureMonths;
    }

    public void rejectLoan() {
        status = "REJECTED";
    }

    public void makeRepayment(double payment) {
        outstandingBalance -= payment;

        if (outstandingBalance < 0.01) {
            outstandingBalance = 0;
            status = "CLOSED";
        }
    }

    public String toFileString() {
        return loanId + "|" + customerId + "|" + loanType + "|"
                + amount + "|" + annualInterestRate + "|"
                + tenureMonths + "|" + status + "|"
                + outstandingBalance;
    }

    public void displayLoanDetails() {
        System.out.println("----------------------------------");
        System.out.println("Loan ID: " + loanId);
        System.out.println("Customer ID: " + customerId);
        System.out.println("Loan Type: " + loanType);
        System.out.printf("Loan Amount: Rs. %.2f%n", amount);
        System.out.println("Annual Interest Rate: " + annualInterestRate + "%");
        System.out.println("Tenure: " + tenureMonths + " months");
        System.out.println("Status: " + status);

        if (status.equals("APPROVED") || status.equals("CLOSED")) {
            System.out.printf("Outstanding Balance: Rs. %.2f%n",
                    outstandingBalance);
        }

        System.out.println("----------------------------------");
    }
}