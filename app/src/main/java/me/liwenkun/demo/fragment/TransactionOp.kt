package me.liwenkun.demo.fragment;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import me.liwenkun.demo.R;

public enum TransactionOp {
    ADD("Add") {
        @Override
        public TransactionOpAction generateTransactionOp(FragmentManager fragmentManager, FragmentTransaction fragmentTransaction, String tag) {
            return new TransactionOpAction(this, tag) {
                @Override
                public void run() {
                    fragmentTransaction.add(R.id.fragment_container, TestFragment.newInstance(), tag);
                }
            };
        }
    },
    REMOVE("Remove") {
        @Override
        public TransactionOpAction generateTransactionOp(FragmentManager fragmentManager, FragmentTransaction fragmentTransaction, String tag) {
            return new TransactionOpAction(this, tag) {
                @Override
                public void run() {
                    Fragment fragment = fragmentManager.findFragmentByTag(tag);
                    if (fragment != null) {
                        fragmentTransaction.remove(fragment);
                    }
                }
            };
        }
    },
    ATTACH("Attach") {
        @Override
        public TransactionOpAction generateTransactionOp(FragmentManager fragmentManager, FragmentTransaction fragmentTransaction, String tag) {
            return new TransactionOpAction(this, tag) {
                @Override
                public void run() {
                    Fragment fragment = fragmentManager.findFragmentByTag(tag);
                    if (fragment != null) {
                        fragmentTransaction.attach(fragment);
                    }
                }
            };
        }
    },
    DETACH("Detach") {
        @Override
        public TransactionOpAction generateTransactionOp(FragmentManager fragmentManager, FragmentTransaction fragmentTransaction, String tag) {
            return new TransactionOpAction(this, tag) {
                @Override
                public void run() {
                    Fragment fragment = fragmentManager.findFragmentByTag(tag);
                    if (fragment != null) {
                        fragmentTransaction.detach(fragment);
                    }
                }
            };
        }
    },
    REPLACE("Replace") {
        public TransactionOpAction generateTransactionOp(FragmentManager fragmentManager, FragmentTransaction fragmentTransaction, String tag) {
            return new TransactionOpAction(this, tag) {
                @Override
                public void run() {
                    fragmentTransaction.replace(R.id.fragment_container, TestFragment.newInstance(), tag);
                }
            };
        }
    };

    String name;

    TransactionOp(String name) {
        this.name = name;
    }

    public abstract TransactionOpAction generateTransactionOp(FragmentManager fragmentManager, FragmentTransaction fragmentTransaction, String tag);
}