package sv.gamgtav.zaglyshka2;

import androidx.lifecycle.MutableLiveData;

public class MainViewModel {

    private final MutableLiveData<Integer> score = new MutableLiveData<>();


    private static final int STARTING_POINTS = 5000;
    private static final int START_BET = 100;

    public MainViewModel(){
        initGame();
    }

    public int getStartBet(){
        return START_BET;
    }

    public MutableLiveData<Integer> getPoints() {
        return score;
    }

    public void initGame(){
        score.postValue(STARTING_POINTS);
    }

}
