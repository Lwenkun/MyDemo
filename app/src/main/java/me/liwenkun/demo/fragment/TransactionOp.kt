package me.liwenkun.demo.fragment

import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import me.liwenkun.demo.R
import me.liwenkun.demo.fragment.TestFragment.Companion.newInstance

enum class TransactionOp {

    ADD {
        override fun generateTransactionOp(
            fragmentManager: FragmentManager,
            fragmentTransaction: FragmentTransaction,
            tag: String
        ): TransactionOpAction {
            return object : TransactionOpAction(this, tag) {
                override fun run() {
                    fragmentTransaction.add(R.id.fragment_container, newInstance(), tag)
                }
            }
        }
    },
    REMOVE {
        override fun generateTransactionOp(
            fragmentManager: FragmentManager,
            fragmentTransaction: FragmentTransaction,
            tag: String
        ): TransactionOpAction {
            return object : TransactionOpAction(this, tag) {
                override fun run() {
                    val fragment = fragmentManager.findFragmentByTag(tag)
                    if (fragment != null) {
                        fragmentTransaction.remove(fragment)
                    }
                }
            }
        }
    },
    ATTACH {
        override fun generateTransactionOp(
            fragmentManager: FragmentManager,
            fragmentTransaction: FragmentTransaction,
            tag: String
        ): TransactionOpAction {
            return object : TransactionOpAction(this, tag) {
                override fun run() {
                    val fragment = fragmentManager.findFragmentByTag(tag)
                    if (fragment != null) {
                        fragmentTransaction.attach(fragment)
                    }
                }
            }
        }
    },
    DETACH {
        override fun generateTransactionOp(
            fragmentManager: FragmentManager,
            fragmentTransaction: FragmentTransaction,
            tag: String
        ): TransactionOpAction {
            return object : TransactionOpAction(this, tag) {
                override fun run() {
                    val fragment = fragmentManager.findFragmentByTag(tag)
                    if (fragment != null) {
                        fragmentTransaction.detach(fragment)
                    }
                }
            }
        }
    },
    REPLACE {
        override fun generateTransactionOp(
            fragmentManager: FragmentManager,
            fragmentTransaction: FragmentTransaction,
            tag: String
        ): TransactionOpAction {
            return object : TransactionOpAction(this, tag) {
                override fun run() {
                    fragmentTransaction.replace(R.id.fragment_container, newInstance(), tag)
                }
            }
        }
    };

    abstract fun generateTransactionOp(
        fragmentManager: FragmentManager,
        fragmentTransaction: FragmentTransaction,
        tag: String
    ): TransactionOpAction
}