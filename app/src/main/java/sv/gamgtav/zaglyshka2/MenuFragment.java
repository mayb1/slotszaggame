package sv.gamgtav.zaglyshka2;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

public class MenuFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_menu, container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button bExit = view.findViewById(R.id.bExit);
        Button bPrivacyPolicy = view.findViewById(R.id.bPrivacyPolicy);
        Button bStartGame = view.findViewById(R.id.bStartGame);

        bExit.setOnClickListener(view1 -> requireActivity().finishAffinity());
        bPrivacyPolicy.setOnClickListener(view12 -> Toast.makeText(getContext(), "Not this time, dude", Toast.LENGTH_LONG).show());
        bStartGame.setOnClickListener(view13 -> Navigation.findNavController(requireView()).navigate(R.id.navigation_game));

    }

}