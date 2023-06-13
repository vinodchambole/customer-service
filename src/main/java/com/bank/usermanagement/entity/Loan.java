package com.bank.usermanagement.entity;

import jakarta.persistence.*;


@Entity
@Table(name = "loans")
public class Loan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "loan_amount")
    private double loanAmount;

    @Column(name = "purpose")
    private String purpose;

    @Column(name = "credit_check_required")
    private boolean creditCheckRequired;

    @Column(name = "collateral_required")
    private boolean collateralRequired;

    private LoanStatus loanStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    private Account account;

    // Other necessary fields such as customer information, loan status, etc.

    // Constructors, getters, and setters

    public Loan() {
        // Default constructor
    }

    public Loan(double loanAmount, String purpose, boolean creditCheckRequired, boolean collateralRequired) {
        this.loanAmount = loanAmount;
        this.purpose = purpose;
        this.creditCheckRequired = creditCheckRequired;
        this.collateralRequired = collateralRequired;
    }

    // Getters and setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public double getLoanAmount() {
        return loanAmount;
    }

    public void setLoanAmount(double loanAmount) {
        this.loanAmount = loanAmount;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public boolean isCreditCheckRequired() {
        return creditCheckRequired;
    }

    public void setCreditCheckRequired(boolean creditCheckRequired) {
        this.creditCheckRequired = creditCheckRequired;
    }

    public boolean isCollateralRequired() {
        return collateralRequired;
    }

    public void setCollateralRequired(boolean collateralRequired) {
        this.collateralRequired = collateralRequired;
    }

}
