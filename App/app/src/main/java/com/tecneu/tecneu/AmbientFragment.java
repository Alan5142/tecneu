package com.tecneu.tecneu;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.security.cert.CertificateException;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AmbientFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AmbientFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AmbientFragment extends Fragment {
    private OnFragmentInteractionListener mListener;
    private Socket mSocket;

    private final TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
            return new java.security.cert.X509Certificate[]{};
        }

        public void checkClientTrusted(X509Certificate[] chain,
                                       String authType) {
        }

        public void checkServerTrusted(X509Certificate[] chain,
                                       String authType) {
        }
    }};

    {
        IO.Options options = new IO.Options();
        try {
            SSLContext mySSLContext = SSLContext.getInstance("TLS");
            mySSLContext.init(null, trustAllCerts, null);
            IO.setDefaultSSLContext(SSLContext.getDefault());

            // Set default hostname
            HostnameVerifier hostnameVerifier = new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            };
            IO.setDefaultHostnameVerifier(hostnameVerifier);

            // set as an option
            options.sslContext = SSLContext.getDefault();
            options.sslContext = mySSLContext;
            options.hostnameVerifier = hostnameVerifier;
            options.secure = true;
            options.port = 3000;
            mSocket = IO.socket("https://192.168.100.8:3000", options);
        } catch (URISyntaxException e) {
            Log.d(AmbientFragment.class.getName(), "No se pudo establecer la comunicación con socketio");
        } catch (NoSuchAlgorithmException ignored) {
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }
    }

    public AmbientFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static AmbientFragment newInstance() {
        AmbientFragment fragment = new AmbientFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_ambient, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TextView temperature = view.findViewById(R.id.ambient_fragment_temperature);
        TextView humidity = view.findViewById(R.id.ambient_fragment_humidity);
        TextView gas = view.findViewById(R.id.ambient_fragment_gas);

        mSocket.on("receiveData", args -> getActivity().runOnUiThread(() -> {
            JSONObject object = (JSONObject) args[0];
            try {
                temperature.setText(object.getString("temp") + "°C");
                humidity.setText(object.getString("humidity") + "%");
                gas.setText(object.getString("gas"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }));

        mSocket.connect();
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
        mSocket.disconnect();
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
