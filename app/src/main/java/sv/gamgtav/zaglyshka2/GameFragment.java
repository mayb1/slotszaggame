package sv.gamgtav.zaglyshka2;

import android.content.pm.ActivityInfo;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;

import android.os.CountDownTimer;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.Locale;
import java.util.Objects;
import java.util.Random;

import sv.gamgtav.zaglyshka2.databinding.FragmentGameBinding;

public class GameFragment extends Fragment {

    private FragmentGameBinding binding;

    private Animation scaleAnimation;

    private MainViewModel mainViewModel;
    private MutableLiveData<Integer> score;

    private int bet;

    private int bonusGameCounter = 0;

    private Random rnd;

    private ImageView[] slotItems;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        mainViewModel = ((MainActivity) requireActivity()).mainViewModel;
        score = mainViewModel.getPoints();

        bet = mainViewModel.getStartBet();

        Objects.requireNonNull(getActivity()).setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        binding = FragmentGameBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rnd = new Random();

        score.observe(getViewLifecycleOwner(), score -> {
            if(score < bet){
                Toast.makeText(getContext(), "Domn! You loose too many points. Lets add some points", Toast.LENGTH_SHORT).show();
                score += 2000;
                binding.tvScore.setText(String.format(Locale.getDefault(), "%d", score));
            } else {
                binding.tvScore.setText(String.format(Locale.getDefault(), "%d", score));
            }

        });


        binding.ivBonusImage.setVisibility(View.INVISIBLE);
        binding.ivBonusBox1.setVisibility(View.INVISIBLE);
        binding.ivBonusBox2.setVisibility(View.INVISIBLE);
        binding.ivBonusBox3.setVisibility(View.INVISIBLE);
        binding.ivBonusBox4.setVisibility(View.INVISIBLE);

        scaleAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.winscale);
        scaleAnimation.setDuration(500);

        slotItems = new ImageView[]{binding.ivSlotIcon1, binding.ivSlotIcon2, binding.ivSlotIcon3,
                binding.ivSlotIcon4, binding.ivSlotIcon5, binding.ivSlotIcon6, binding.ivSlotIcon7,
                binding.ivSlotIcon8, binding.ivSlotIcon9, binding.ivSlotIcon10, binding.ivSlotIcon11,
                binding.ivSlotIcon12, binding.ivSlotIcon13, binding.ivSlotIcon14, binding.ivSlotIcon15,
                binding.ivSlotIcon16, binding.ivSlotIcon17, binding.ivSlotIcon18, binding.ivSlotIcon19,
                binding.ivSlotIcon20};

        binding.bSpin.setOnClickListener(view1 -> {
            if (bonusGameCounter >= 20) {
                startBonusGame();
            } else {
                bonusGameCounter++;
                startSlotsGame();
            }

        });
        Button[] betButtons = new Button[]{binding.bBet100, binding.bBet250, binding.bBet500};
        for (Button betButton : betButtons) {
            betButton.setOnClickListener(v -> {
                int checkScore = Integer.parseInt(v.getTag().toString());
                if (score.getValue() < checkScore) {
                    Toast.makeText(getContext(), "Not enough points", Toast.LENGTH_SHORT).show();
                } else {
                    bet = checkScore;
                    Toast.makeText(getContext(), "Bet set to " + bet, Toast.LENGTH_SHORT).show();
                }

            });
        }
        ImageView[] bonusButtons = new ImageView[] {binding.ivBonusBox1, binding.ivBonusBox2, binding.ivBonusBox3, binding.ivBonusBox4};
        for (ImageView bonusButton : bonusButtons) {
            bonusButton.setOnClickListener(view12 -> {
                boolean win;
                switch((int) (Math.random() * 2)) {
                    case 0:
                        bonusButton.setImageResource(R.drawable.bonuswin);
                        win = true;
                        break;
                    case 1:
                        bonusButton.setImageResource(R.drawable.bonusloose);
                        win = false;
                        break;
                    default:
                        throw new IllegalStateException("Unexpected value: "
                                + (int) (Math.random() * 4));
                }
                if (win){
                    Toast.makeText(getContext(), "Great! You win 5000 points", Toast.LENGTH_SHORT).show();
                    score.postValue(score.getValue() + 5000);
                } else {
                    Toast.makeText(getContext(), "You loose :(", Toast.LENGTH_SHORT).show();
                }
                Handler handler = new Handler();
                handler.postDelayed(this::endBonusGame, 1000);
            });
        }

    }


    public void startSlotsGame() {
        binding.bSpin.setEnabled(false);

        new CountDownTimer(3000, 100) {
            @Override
            public void onTick(long l) {
                int[] imageSlot = {R.drawable.icon_2, R.drawable.icon_3, R.drawable.icon_5,
                        R.drawable.icon_6, R.drawable.icon_7};
                for (ImageView item : slotItems) {
                    int random = (int) (Math.random() * 5);
                    item.setImageResource(imageSlot[random]);
                    item.setTag(imageSlot[random]);
                }
            }

            @Override
            public void onFinish() {
                binding.bSpin.setEnabled(true);
                checkGameWinner();
            }
        }.start();
    }

    private void checkGameWinner() {

        int value;
        int s1 = 0;
        int s2 = 0;
        int s3 = 0;
        int s4 = 0;
        int s5 = 0;

        for (ImageView slotItem : slotItems) {
            if (slotItem.getTag().equals(R.drawable.icon_2)) {
                s1++;
            } else if (slotItem.getTag().equals(R.drawable.icon_3)) {
                s2++;
            } else if (slotItem.getTag().equals(R.drawable.icon_5)) {
                s3++;
            } else if (slotItem.getTag().equals(R.drawable.icon_6)) {
                s4++;
            } else if (slotItem.getTag().equals(R.drawable.icon_7)) {
                s5++;
            }
        }

        int winner;
        if (s1 > 5 && s1 > s2 && s1 > s3 && s1 > s4 && s1 > s5) {
            winner = 1;
        } else if (s2 > 5 && s2 > s1 && s2 > s3 && s2 > s4 && s2 > s5) {
            winner = 2;
        } else if (s3 > 5 && s3 > s2 && s3 > s4 && s3 > s5) {
            winner = 3;
        } else if (s4 > 5 && s4 > s2 && s4 > s3 && s4 > s5) {
            winner = 4;
        } else if (s5 > 5 && s5 > s2 && s5 > s3 && s5 > s4) {
            winner = 5;
        } else {
            winner = 0;
        }

        switch (winner) {
            case 0:
                value = -bet;
                break;
            case 1:
                value = bet * 2;
                for (ImageView element : slotItems) {
                    if (element.getTag().equals(R.drawable.icon_2)) {
                        element.startAnimation(scaleAnimation);
                    }
                }
                break;
            case 2:
                value = bet * 3;
                for (ImageView view : slotItems) {
                    if (view.getTag().equals(R.drawable.icon_3)) {
                        view.startAnimation(scaleAnimation);
                    }
                }
                break;
            case 3:
                value = bet * 4;
                for (ImageView imageView : slotItems) {
                    if (imageView.getTag().equals(R.drawable.icon_5)) {
                        imageView.startAnimation(scaleAnimation);
                    }
                }
                break;
            case 4:
                value = bet * 5;
                for (ImageView item : slotItems) {
                    if (item.getTag().equals(R.drawable.icon_6)) {
                        item.startAnimation(scaleAnimation);
                    }
                }
                break;
            case 5:
                value = bet * 6;
                for (ImageView slotItem : slotItems) {
                    if (slotItem.getTag().equals(R.drawable.icon_7)) {
                        slotItem.startAnimation(scaleAnimation);
                    }
                }
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + winner);
        }

        score.postValue(score.getValue() + value);
    }

    private void startBonusGame(){
        for (ImageView slotItem : slotItems) {
            slotItem.setVisibility(View.INVISIBLE);
        }
        binding.ivBaraban.setVisibility(View.INVISIBLE);
        binding.bBet100.setVisibility(View.INVISIBLE);
        binding.bBet250.setVisibility(View.INVISIBLE);
        binding.bBet500.setVisibility(View.INVISIBLE);
        binding.bSpin.setVisibility(View.INVISIBLE);
        binding.tvPoints.setVisibility(View.INVISIBLE);
        binding.tvScore.setVisibility(View.INVISIBLE);

        binding.ivBonusImage.setVisibility(View.VISIBLE);
        binding.ivBonusBox1.setVisibility(View.VISIBLE);
        binding.ivBonusBox2.setVisibility(View.VISIBLE);
        binding.ivBonusBox3.setVisibility(View.VISIBLE);
        binding.ivBonusBox4.setVisibility(View.VISIBLE);
    }

    private void endBonusGame(){
        for (ImageView slotItem : slotItems) {
            slotItem.setVisibility(View.VISIBLE);
        }
        binding.ivBaraban.setVisibility(View.VISIBLE);
        binding.bBet100.setVisibility(View.INVISIBLE);
        binding.bBet250.setVisibility(View.INVISIBLE);
        binding.bBet500.setVisibility(View.INVISIBLE);
        binding.bSpin.setVisibility(View.VISIBLE);
        binding.tvPoints.setVisibility(View.VISIBLE);
        binding.tvScore.setVisibility(View.VISIBLE);

        binding.ivBonusImage.setVisibility(View.INVISIBLE);
        binding.ivBonusBox1.setVisibility(View.INVISIBLE);
        binding.ivBonusBox2.setVisibility(View.INVISIBLE);
        binding.ivBonusBox3.setVisibility(View.INVISIBLE);
        binding.ivBonusBox4.setVisibility(View.INVISIBLE);
    }
}