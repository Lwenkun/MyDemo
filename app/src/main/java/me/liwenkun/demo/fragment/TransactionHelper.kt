package me.liwenkun.demo.fragment;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.List;

public class TransactionHelper {

    private final FragmentManager fragmentManager;

    private final MutableLiveData<List<TransactionOpAction>> transactionOps = new MutableLiveData<>();
    private FragmentTransaction currentFragmentTransaction;

    public TransactionHelper(FragmentManager fragmentManager) {
        this.fragmentManager = fragmentManager;
    }

    public void addTransactionOp(TransactionOp transactionOp, String tag) {
        if (currentFragmentTransaction == null) {
            currentFragmentTransaction = fragmentManager.beginTransaction();
        }
        addTransactionOpInternal(transactionOp.generateTransactionOp(fragmentManager, currentFragmentTransaction, tag));
    }

    public void addTransactionOpInternal(TransactionOpAction runnable) {
        List<TransactionOpAction> transactions = transactionOps.getValue() == null
                ? new ArrayList<>() : transactionOps.getValue();
        transactions.add(runnable);
        transactionOps.setValue(transactions);
    }

    public void cleanTransactionOp() {
        List<TransactionOpAction> transactions = transactionOps.getValue();
        if (transactions != null) {
            transactions.clear();
            transactionOps.setValue(transactions);
        }
    }

    public LiveData<List<TransactionOpAction>> getTransactionOp() {
        return transactionOps;
    }

    public boolean commit(boolean addToBackStack) {
        if (transactionOps.getValue() == null || transactionOps.getValue().isEmpty()) {
            return false;
        }
        for (TransactionOpAction transactionOpAction : transactionOps.getValue()) {
            transactionOpAction.run();
        }
        cleanTransactionOp();
        if (addToBackStack) {
            currentFragmentTransaction.addToBackStack(null);
        }
        currentFragmentTransaction.commit();
        currentFragmentTransaction = null;
        return true;
    }
}
