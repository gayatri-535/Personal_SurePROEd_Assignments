public class EMICalculator {

    public static double calculateEMI(
            double principal,
            double annualInterestRate,
            int tenureMonths) {

        if (principal <= 0 || tenureMonths <= 0 || annualInterestRate < 0) {
            return 0;
        }

        double monthlyRate = annualInterestRate / (12 * 100);

        if (monthlyRate == 0) {
            return principal / tenureMonths;
        }

        double factor = Math.pow(1 + monthlyRate, tenureMonths);

        return principal * monthlyRate * factor / (factor - 1);
    }

    public static double calculateTotalPayment( double principal, double annualInterestRate, int tenureMonths) {

        return calculateEMI( principal, annualInterestRate, tenureMonths) * tenureMonths;
    }

    public static double calculateTotalInterest( double principal,double annualInterestRate,int tenureMonths) {

        return calculateTotalPayment(principal, annualInterestRate, tenureMonths) - principal;
    }
}