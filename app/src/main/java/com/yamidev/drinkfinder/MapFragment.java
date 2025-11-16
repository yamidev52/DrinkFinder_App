package com.yamidev.drinkfinder;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapFragment extends Fragment implements OnMapReadyCallback {

    private GoogleMap mMap;
    private FusedLocationProviderClient fusedLocationClient;

    private final ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    getCurrentLocationAndSetupMap();
                } else {
                    Toast.makeText(requireContext(), "Permiso de ubicación denegado.", Toast.LENGTH_LONG).show();
                }
            });

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_map, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Toolbar toolbar = view.findViewById(R.id.toolbar_map);
        ((AppCompatActivity) requireActivity()).setSupportActionBar(toolbar);

        NavController navController = NavHostFragment.findNavController(this);
        NavigationUI.setupActionBarWithNavController(((AppCompatActivity) requireActivity()), navController);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity());

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map_container);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        checkLocationPermission();
    }

    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            getCurrentLocationAndSetupMap();
        } else {
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION);
        }
    }

    @SuppressWarnings("MissingPermission")
    private void getCurrentLocationAndSetupMap() {
        mMap.setMyLocationEnabled(true);

        fusedLocationClient.getLastLocation().addOnSuccessListener(requireActivity(), location -> {
            if (location != null) {
                LatLng userLocation = new LatLng(location.getLatitude(), location.getLongitude());
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 15f));

                addMockMarkers(userLocation);
            } else {
                Toast.makeText(requireContext(), "No se pudo obtener la ubicación. Activa la ubicación del dispositivo.", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void addMockMarkers(LatLng userLocation) {
        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(userLocation.latitude + 0.001, userLocation.longitude + 0.001))
                .title("Bar Falso 1")
                .snippet("Toca para ver detalles"));

        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(userLocation.latitude - 0.002, userLocation.longitude + 0.003))
                .title("Cantina de Moe")
                .snippet("Happy Hour de 5 a 7"));

        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(userLocation.latitude + 0.004, userLocation.longitude - 0.002))
                .title("El Buen Beber")
                .snippet("Música en vivo los viernes"));


        mMap.setOnMarkerClickListener(marker -> {
            Toast.makeText(requireContext(), "Has tocado: " + marker.getTitle(), Toast.LENGTH_SHORT).show();
            return false;
        });
    }
}