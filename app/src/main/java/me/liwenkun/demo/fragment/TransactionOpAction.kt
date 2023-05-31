package me.liwenkun.demo.fragment

abstract class TransactionOpAction(
    private val transactionOp: TransactionOp,
    private val tag: String
) : Runnable {
    val actionDesc: String
        get() = transactionOp.name + ' ' + tag
}