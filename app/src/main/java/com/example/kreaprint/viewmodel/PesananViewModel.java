package com.example.kreaprint.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.example.kreaprint.model.Pesanan;
import java.util.ArrayList;
import java.util.List;

public class PesananViewModel extends ViewModel {
    private final MutableLiveData<List<Pesanan>> pesananList = new MutableLiveData<>(new ArrayList<>());

    public LiveData<List<Pesanan>> getPesananList() {
        return pesananList;
    }

    public void tambahPesanan(Pesanan pesanan) {
        List<Pesanan> currentList = pesananList.getValue();
        if (currentList != null) {
            currentList.add(pesanan);
            pesananList.setValue(currentList);
        }
    }

    public void hapusPesanan(Pesanan pesanan) {
        List<Pesanan> list = pesananList.getValue();
        if (list != null && list.contains(pesanan)) {
            list.remove(pesanan);
            pesananList.setValue(list);
        }
    }

    public void hapusPesanan(int index) {
        List<Pesanan> list = pesananList.getValue();
        if (list != null && index >= 0 && index < list.size()) {
            list.remove(index);
            pesananList.setValue(list);
        }
    }
}
