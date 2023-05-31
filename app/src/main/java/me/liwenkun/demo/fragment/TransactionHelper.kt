package me.liwenkun.demo.fragment

import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class TransactionHelper(private val fragmentManager: FragmentManager) {

    fun size(): Int {
        return transactionOps.value?.size ?: 0
    }

    private val transactionOps = MutableLiveData<MutableList<TransactionOpAction>?>()
    private var currentFragmentTransaction: FragmentTransaction? = null

    fun addTransactionOp(transactionOp: TransactionOp, tag: String) {
        if (currentFragmentTransaction == null) {
            currentFragmentTransaction = fragmentManager.beginTransaction()
        }
        addTransactionOpInternal(
            transactionOp.generateTransactionOp(
                fragmentManager,
                currentFragmentTransaction!!,
                tag
            )
        )
    }

    fun addTransactionOpInternal(runnable: TransactionOpAction) {
        val transactions = if (transactionOps.value == null) ArrayList() else transactionOps.value!!
        transactions.add(runnable)
        transactionOps.value = transactions
    }

    fun cleanTransactionOp() {
        val transactions = transactionOps.value
        if (transactions != null) {
            transactions.clear()
            transactionOps.value = transactions
        }
    }

    val transactionOp: LiveData<MutableList<TransactionOpAction>?>
        get() = transactionOps

    fun commit(addToBackStack: Boolean) {
        transactionOps.value?.takeIf { it.isEmpty() }.apply {
            for (transactionOpAction in transactionOps.value!!) {
                transactionOpAction.run()
            }
            cleanTransactionOp()
            if (addToBackStack) {
                currentFragmentTransaction!!.addToBackStack(null)
            }
            currentFragmentTransaction!!.commit()
            currentFragmentTransaction = null
        }
    }
}