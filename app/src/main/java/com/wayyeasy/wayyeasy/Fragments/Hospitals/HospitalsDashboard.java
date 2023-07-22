package com.wayyeasy.wayyeasy.Fragments.Hospitals;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.wayyeasy.wayyeasy.Adapters.Hospitals.HospitalData;
import com.wayyeasy.wayyeasy.ComponentFiles.ApiHandlers.ApiControllers;
import com.wayyeasy.wayyeasy.ComponentFiles.Constants.Constants;
import com.wayyeasy.wayyeasy.ComponentFiles.PreferenceManager.SharedPreferenceManager;
import com.wayyeasy.wayyeasy.Models.Hospitals.hospital_response_model;
import com.wayyeasy.wayyeasy.databinding.FragmentHospitalsDashboardBinding;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HospitalsDashboard extends Fragment {

    private static final String TAG = "Hospitals Dashboard";
    FragmentHospitalsDashboardBinding dashboardHospital;

    SharedPreferenceManager preferenceManager;

    List<hospital_response_model> topArrayList = new ArrayList<>();
    List<hospital_response_model> valueForMoneyArrayList = new ArrayList<>();
    List<hospital_response_model> locationArrayList = new ArrayList<>();
    List<hospital_response_model> treatmentArrayList = new ArrayList<>();
    List<hospital_response_model> staffArrayList = new ArrayList<>();
    HospitalData hospitalData;

    private String id, token;

    private int topLastCount = 0, valueForMoneyLastCount = 0, locationLastCount = 0, treatmentLastCount = 0, staffsLastCount = 0;
    private int limit = 3, topPageCount = 1, valForMoneyPageCount = 1, locationPageCount = 1, treatmentPageCount = 1, staffsPageCount = 1;

    public HospitalsDashboard() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        dashboardHospital = FragmentHospitalsDashboardBinding.inflate(inflater, container, false);
        preferenceManager = new SharedPreferenceManager(getContext());

        if (preferenceManager.getBoolean(Constants.KEY_IS_SIGNED_IN)) {
            token = preferenceManager.getString(Constants.KEY_MONGO_TOKEN);
            id = preferenceManager.getString(Constants.KEY_MONGO_ID);
        }

        hospitalData = new HospitalData(staffArrayList, getActivity().getApplicationContext());
        dashboardHospital.recyclerStaffs.setAdapter(hospitalData);

        fetchAllHospitals(token, "4", limit, 1);

        dashboardHospital.recyclerTop.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                LinearLayoutManager llm = (LinearLayoutManager) dashboardHospital.recyclerTop.getLayoutManager();
                if (llm.findLastCompletelyVisibleItemPosition() > topLastCount) {
                    if (llm.findLastCompletelyVisibleItemPosition() % 2 == 0) {
                        topPageCount++;
                        fetchTopHospitals(token, "4", limit, topPageCount);
                        topLastCount = llm.findLastCompletelyVisibleItemPosition();
                    }
                }
            }
        });

        dashboardHospital.recyclerValueForMoney.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                LinearLayoutManager llm = (LinearLayoutManager) dashboardHospital.recyclerValueForMoney.getLayoutManager();
                if (llm.findLastCompletelyVisibleItemPosition() > valueForMoneyLastCount) {
                    if (llm.findLastCompletelyVisibleItemPosition() % 2 == 0) {
                        valForMoneyPageCount++;
                        fetchValurForMoneyHospitals(token, "4", limit, valForMoneyPageCount);
                        valueForMoneyLastCount = llm.findLastCompletelyVisibleItemPosition();
                    }
                }
            }
        });

        dashboardHospital.recyclerLocation.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                LinearLayoutManager llm = (LinearLayoutManager) dashboardHospital.recyclerLocation.getLayoutManager();
                if (llm.findLastCompletelyVisibleItemPosition() > locationLastCount) {
                    if (llm.findLastCompletelyVisibleItemPosition() % 2 == 0) {
                        locationPageCount++;
                        fetchLocationHospitals(token, "4", limit, locationPageCount);
                        locationLastCount = llm.findLastCompletelyVisibleItemPosition();
                    }
                }
            }
        });

        dashboardHospital.recyclerTreatment.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                LinearLayoutManager llm = (LinearLayoutManager) dashboardHospital.recyclerTreatment.getLayoutManager();
                if (llm.findLastCompletelyVisibleItemPosition() > treatmentLastCount) {
                    if (llm.findLastCompletelyVisibleItemPosition() % 2 == 0) {
                        treatmentPageCount++;
                        fetchTreatmentHospitals(token, "4", limit, treatmentPageCount);
                        treatmentLastCount = llm.findLastCompletelyVisibleItemPosition();
                    }
                }
            }
        });

        dashboardHospital.recyclerStaffs.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                LinearLayoutManager llm = (LinearLayoutManager) dashboardHospital.recyclerStaffs.getLayoutManager();
                if (llm.findLastCompletelyVisibleItemPosition() > staffsLastCount) {
                    if (llm.findLastCompletelyVisibleItemPosition() % 2 == 0) {
                        staffsPageCount++;
                        fetchStaffHospital(token, "4", limit, staffsPageCount);
                        staffsLastCount = llm.findLastCompletelyVisibleItemPosition();
                    }
                }
            }
        });

        return dashboardHospital.getRoot();
    }

    public void fetchNearbyHospitals(String pinCode) {
        Call<List<hospital_response_model>> pincode = ApiControllers
                .getInstance()
                .getApi()
                .getFilteredHospital("Bearer " + token, "pincode", pinCode, limit, 1);

        pincode.enqueue(new Callback<List<hospital_response_model>>() {
            @Override
            public void onResponse(Call<List<hospital_response_model>> call, Response<List<hospital_response_model>> response) {
                dashboardHospital.shimmerFrame1.stopShimmer();
                dashboardHospital.shimmerFrame1.setVisibility(View.GONE);
                dashboardHospital.recyclerClose.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));
                if (response.body().size() > 0) {
                    dashboardHospital.noData.setVisibility(View.GONE);
                    List<hospital_response_model> data = response.body();
                    hospitalData = new HospitalData(data, getActivity().getApplicationContext());
                    dashboardHospital.recyclerClose.setAdapter(hospitalData);
                } else dashboardHospital.noData.setVisibility(View.VISIBLE);
            }

            @Override
            public void onFailure(Call<List<hospital_response_model>> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.toString());
            }
        });
    }

    public void shutNearbyShimmer() {
        dashboardHospital.shimmerFrame1.stopShimmer();
        dashboardHospital.shimmerFrame1.setVisibility(View.GONE);
        dashboardHospital.noData.setVisibility(View.VISIBLE);
    }

    private void fetchTreatmentHospitals(String token, String value, int limit, int page) {
        Call<List<hospital_response_model>> treatment = ApiControllers
                .getInstance()
                .getApi()
                .getFilteredHospital("Bearer " + token, "treatment", value, limit, page);

        treatment.enqueue(new Callback<List<hospital_response_model>>() {
            @Override
            public void onResponse(Call<List<hospital_response_model>> call, Response<List<hospital_response_model>> response) {
                dashboardHospital.shimmerFrame5.stopShimmer();
                dashboardHospital.shimmerFrame5.setVisibility(View.GONE);
                dashboardHospital.recyclerTreatment.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));
                List<hospital_response_model> data = response.body();
                dashboardHospital.recyclerTreatment.scrollToPosition(treatmentArrayList.size() - 2);
                treatmentArrayList.addAll(data);
                hospitalData = new HospitalData(treatmentArrayList, getActivity().getApplicationContext());
                dashboardHospital.recyclerTreatment.setAdapter(hospitalData);
            }

            @Override
            public void onFailure(Call<List<hospital_response_model>> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.toString());
            }
        });
    }

    private void fetchLocationHospitals(String token, String value, int limit, int page) {
        Call<List<hospital_response_model>> location = ApiControllers
                .getInstance()
                .getApi()
                .getFilteredHospital("Bearer " + token, "location", value, limit, page);

        location.enqueue(new Callback<List<hospital_response_model>>() {
            @Override
            public void onResponse(Call<List<hospital_response_model>> call, Response<List<hospital_response_model>> response) {
                dashboardHospital.shimmerFrame4.stopShimmer();
                dashboardHospital.shimmerFrame4.setVisibility(View.GONE);
                dashboardHospital.recyclerLocation.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));
                List<hospital_response_model> data = response.body();
                dashboardHospital.recyclerLocation.scrollToPosition(locationArrayList.size() - 2);
                locationArrayList.addAll(data);
                hospitalData = new HospitalData(locationArrayList, getActivity().getApplicationContext());
                dashboardHospital.recyclerLocation.setAdapter(hospitalData);
            }

            @Override
            public void onFailure(Call<List<hospital_response_model>> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.toString());
            }
        });
    }

    private void fetchValurForMoneyHospitals(String token, String value, int limit, int page) {
        Call<List<hospital_response_model>> valueForMoney = ApiControllers
                .getInstance()
                .getApi()
                .getFilteredHospital("Bearer " + token, "valueForMoney", value, limit, page);

        valueForMoney.enqueue(new Callback<List<hospital_response_model>>() {
            @Override
            public void onResponse(Call<List<hospital_response_model>> call, Response<List<hospital_response_model>> response) {
                dashboardHospital.shimmerFrame3.stopShimmer();
                dashboardHospital.shimmerFrame3.setVisibility(View.GONE);
                dashboardHospital.recyclerValueForMoney.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));
                List<hospital_response_model> data = response.body();
                dashboardHospital.recyclerValueForMoney.scrollToPosition(valueForMoneyArrayList.size() - 2);
                valueForMoneyArrayList.addAll(data);
                hospitalData = new HospitalData(valueForMoneyArrayList, getActivity().getApplicationContext());
                dashboardHospital.recyclerValueForMoney.setAdapter(hospitalData);
            }

            @Override
            public void onFailure(Call<List<hospital_response_model>> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.toString());
            }
        });
    }

    private void fetchTopHospitals(String token, String value, int limit, int page) {
        Call<List<hospital_response_model>> top = ApiControllers
                .getInstance()
                .getApi()
                .getFilteredHospital("Bearer " + token, "top", value, limit, page);

        top.enqueue(new Callback<List<hospital_response_model>>() {
            @Override
            public void onResponse(Call<List<hospital_response_model>> call, Response<List<hospital_response_model>> response) {
                dashboardHospital.shimmerFrame2.stopShimmer();
                dashboardHospital.shimmerFrame2.setVisibility(View.GONE);
                dashboardHospital.recyclerTop.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));
                List<hospital_response_model> data = response.body();
                dashboardHospital.recyclerTop.scrollToPosition(topArrayList.size() - 2);
                topArrayList.addAll(data);
                hospitalData = new HospitalData(topArrayList, getActivity().getApplicationContext());
                dashboardHospital.recyclerTop.setAdapter(hospitalData);
            }

            @Override
            public void onFailure(Call<List<hospital_response_model>> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.toString());
            }
        });
    }

    private void fetchStaffHospital(String token, String value, int limit, int page) {
        Call<List<hospital_response_model>> staffs = ApiControllers
                .getInstance()
                .getApi()
                .getFilteredHospital("Bearer " + token, "staffs", value, limit, page);

        staffs.enqueue(new Callback<List<hospital_response_model>>() {
            @Override
            public void onResponse(Call<List<hospital_response_model>> call, Response<List<hospital_response_model>> response) {
                dashboardHospital.shimmerFrame6.stopShimmer();
                dashboardHospital.shimmerFrame6.setVisibility(View.GONE);
                dashboardHospital.recyclerStaffs.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));
                List<hospital_response_model> data = response.body();
                dashboardHospital.recyclerStaffs.scrollToPosition(staffArrayList.size() - 2);
                staffArrayList.addAll(data);
                hospitalData = new HospitalData(staffArrayList, getActivity().getApplicationContext());
                dashboardHospital.recyclerStaffs.setAdapter(hospitalData);
            }

            @Override
            public void onFailure(Call<List<hospital_response_model>> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.toString());
            }
        });
    }

    private void fetchAllHospitals(String token, String value, int limit, int page) {
        //Top listed hospitals
        Call<List<hospital_response_model>> top = ApiControllers
                .getInstance()
                .getApi()
                .getFilteredHospital("Bearer " + token, "top", value, limit, page);

        top.enqueue(new Callback<List<hospital_response_model>>() {
            @Override
            public void onResponse(Call<List<hospital_response_model>> call, Response<List<hospital_response_model>> response) {
                dashboardHospital.shimmerFrame2.stopShimmer();
                dashboardHospital.shimmerFrame2.setVisibility(View.GONE);
                try {
                    dashboardHospital.recyclerTop.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));
                    List<hospital_response_model> data = response.body();
                    dashboardHospital.recyclerTop.scrollToPosition(topArrayList.size() - 2);
                    topArrayList.addAll(data);
                    hospitalData = new HospitalData(topArrayList, getActivity().getApplicationContext());
                    dashboardHospital.recyclerTop.setAdapter(hospitalData);
                } catch (Exception e) {
                    Log.d(TAG, "onResponse: 403 " + e.toString());
                }
            }

            @Override
            public void onFailure(Call<List<hospital_response_model>> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.toString());
            }
        });

        //Value for money
        Call<List<hospital_response_model>> valueForMoney = ApiControllers
                .getInstance()
                .getApi()
                .getFilteredHospital("Bearer " + token, "valueForMoney", value, limit, page);

        valueForMoney.enqueue(new Callback<List<hospital_response_model>>() {
            @Override
            public void onResponse(Call<List<hospital_response_model>> call, Response<List<hospital_response_model>> response) {
                dashboardHospital.shimmerFrame3.stopShimmer();
                dashboardHospital.shimmerFrame3.setVisibility(View.GONE);
                try {
                    dashboardHospital.recyclerValueForMoney.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));
                    List<hospital_response_model> data = response.body();
                    dashboardHospital.recyclerValueForMoney.scrollToPosition(valueForMoneyArrayList.size() - 2);
                    valueForMoneyArrayList.addAll(data);
                    hospitalData = new HospitalData(valueForMoneyArrayList, getActivity().getApplicationContext());
                    dashboardHospital.recyclerValueForMoney.setAdapter(hospitalData);
                } catch (Exception e) {
                    Log.d(TAG, "onResponse: 403 " + e.toString());
                }
            }

            @Override
            public void onFailure(Call<List<hospital_response_model>> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.toString());
            }
        });

        //Best in location
        Call<List<hospital_response_model>> location = ApiControllers
                .getInstance()
                .getApi()
                .getFilteredHospital("Bearer " + token, "location", value, limit, page);

        location.enqueue(new Callback<List<hospital_response_model>>() {
            @Override
            public void onResponse(Call<List<hospital_response_model>> call, Response<List<hospital_response_model>> response) {
                dashboardHospital.shimmerFrame4.stopShimmer();
                dashboardHospital.shimmerFrame4.setVisibility(View.GONE);
                try {
                    dashboardHospital.recyclerLocation.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));
                    List<hospital_response_model> data = response.body();
                    dashboardHospital.recyclerLocation.scrollToPosition(locationArrayList.size() - 2);
                    locationArrayList.addAll(data);
                    hospitalData = new HospitalData(locationArrayList, getActivity().getApplicationContext());
                    dashboardHospital.recyclerLocation.setAdapter(hospitalData);
                } catch (Exception e) {
                    Log.d(TAG, "onResponse: 403 " + e.toString());
                }
            }

            @Override
            public void onFailure(Call<List<hospital_response_model>> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.toString());
            }
        });

        //Best in treatment
        Call<List<hospital_response_model>> treatment = ApiControllers
                .getInstance()
                .getApi()
                .getFilteredHospital("Bearer " + token, "treatment", value, limit, page);

        treatment.enqueue(new Callback<List<hospital_response_model>>() {
            @Override
            public void onResponse(Call<List<hospital_response_model>> call, Response<List<hospital_response_model>> response) {
                dashboardHospital.shimmerFrame5.stopShimmer();
                dashboardHospital.shimmerFrame5.setVisibility(View.GONE);
                try {
                    dashboardHospital.recyclerTreatment.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));
                    List<hospital_response_model> data = response.body();
                    dashboardHospital.recyclerTreatment.scrollToPosition(treatmentArrayList.size() - 2);
                    treatmentArrayList.addAll(data);
                    hospitalData = new HospitalData(treatmentArrayList, getActivity().getApplicationContext());
                    dashboardHospital.recyclerTreatment.setAdapter(hospitalData);
                } catch (Exception e) {
                    Log.d(TAG, "onResponse: 403 " + e.toString());
                }
            }

            @Override
            public void onFailure(Call<List<hospital_response_model>> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.toString());
            }
        });

        //Best in staffs
        Call<List<hospital_response_model>> staffs = ApiControllers
                .getInstance()
                .getApi()
                .getFilteredHospital("Bearer " + token, "staffs", value, limit, page);

        staffs.enqueue(new Callback<List<hospital_response_model>>() {
            @Override
            public void onResponse(Call<List<hospital_response_model>> call, Response<List<hospital_response_model>> response) {
                dashboardHospital.shimmerFrame6.stopShimmer();
                dashboardHospital.shimmerFrame6.setVisibility(View.GONE);
                try {
                    dashboardHospital.recyclerStaffs.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));
                    List<hospital_response_model> data = response.body();
                    dashboardHospital.recyclerStaffs.scrollToPosition(staffArrayList.size() - 2);
                    staffArrayList.addAll(data);
                    hospitalData = new HospitalData(staffArrayList, getActivity().getApplicationContext());
                    dashboardHospital.recyclerStaffs.setAdapter(hospitalData);
                } catch (Exception e) {
                    Log.d(TAG, "onResponse: 403 " + e.toString());
                }
            }

            @Override
            public void onFailure(Call<List<hospital_response_model>> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.toString());
            }
        });
    }
}