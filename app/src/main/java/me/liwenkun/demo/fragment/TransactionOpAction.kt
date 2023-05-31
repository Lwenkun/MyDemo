package me.liwenkun.demo.fragment;

public abstract class TransactionOpAction implements Runnable {
    private final TransactionOp transactionOp;
    private final String tag;
    public TransactionOpAction(TransactionOp op, String tag) {
        this.transactionOp = op;
        this.tag = tag;
    }

    public String getActionDesc() {
        return transactionOp.name + ' ' + tag;
    }
}
