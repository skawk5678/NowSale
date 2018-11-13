package com.example.yoonsung.nowsale;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.yoonsung.nowsale.VO.AllOwnerClientKeyVO;
import com.example.yoonsung.nowsale.VO.CouponVO;
import com.example.yoonsung.nowsale.VO.IsFavoriteGetCountVO;
import com.example.yoonsung.nowsale.VO.MenuVO;
import com.example.yoonsung.nowsale.http.AllService;
import com.example.yoonsung.nowsale.naverMap.NMapPOIflagType;
import com.example.yoonsung.nowsale.naverMap.NMapViewerResourceProvider;
import com.nhn.android.maps.NMapContext;
import com.nhn.android.maps.NMapController;
import com.nhn.android.maps.NMapView;
import com.nhn.android.maps.maplib.NGeoPoint;
import com.nhn.android.maps.nmapmodel.NMapError;
import com.nhn.android.maps.overlay.NMapPOIdata;
import com.nhn.android.mapviewer.overlay.NMapOverlayManager;
import com.nhn.android.mapviewer.overlay.NMapPOIdataOverlay;
import com.nhn.android.mapviewer.overlay.NMapResourceProvider;

import java.util.List;

import io.github.luizgrp.sectionedrecyclerviewadapter.SectionParameters;
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter;
import io.github.luizgrp.sectionedrecyclerviewadapter.StatelessSection;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.ContentValues.TAG;

/**
 * Created by Junyoung on 2016-06-23.
 */

public class OwnerInfoTabFragment1 extends Fragment {
    private Intent get_intent;
    private LinearLayout backBtn;
    private TextView nameText;
    private TextView phoneText;
    private TextView introduceText,countText;
    private ImageView heart;
    private Boolean favBool;
    private Intent loginNeedPopupIntent;
    private AllOwnerClientKeyVO allOwnerClientKeyVO;
    private CouponVO couponVO;
    private IsFavoriteGetCountVO isFavoriteGetCountVO;
    private SectionedRecyclerViewAdapter sectionAdapter;
    private AllService allService;
    private ScrollView scrollView;
    private List<MenuVO> menuDatas;

    //fagment -> activity 데이터 전달을 위한 데이터
    private OnFavSetListener onFavSetListener;

    //네이버 지도 설정
    private NMapContext mMapContext;
    private static final String CLIENT_ID = Config.naverMapClientID;// 애플리케이션 클라이언트 아이디 값
    private NGeoPoint nGeoPoint;
    private NMapResourceProvider nMapResourceProvider;
    private NMapOverlayManager mapOverlayManager;


//126.914925, 37.528728
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.owner_info_tab_fragment_1, container, false);
        scrollView = view.findViewById(R.id.scrollView);


        couponVO = (CouponVO) getArguments().getSerializable("CouponVO");
        isFavoriteGetCountVO = (IsFavoriteGetCountVO) getArguments().getSerializable("IsFavoriteGetCountVO");
        menuDatas = getArguments().getParcelableArrayList("menuDatas");

        loginNeedPopupIntent = new Intent(getActivity(),LoginCancelPopupActivity.class);

        nameText=(TextView)view.findViewById(R.id.marketName);
        phoneText=(TextView)view.findViewById(R.id.marketPhone);
        introduceText=view.findViewById(R.id.marketIntroduce);

        countText=view.findViewById(R.id.favorite_count);
        heart=view.findViewById(R.id.dangol);



        favBool = isFavoriteGetCountVO.isDangol();
        countText.setText(""+isFavoriteGetCountVO.getDangol_count());

        //상품 이미지를 보여주기 위
        sectionAdapter = new SectionedRecyclerViewAdapter();

        sectionAdapter.addSection(new MovieSection("주요 상품", menuDatas));

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);

        GridLayoutManager glm = new GridLayoutManager(getContext(), 2);
        glm.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                switch (sectionAdapter.getSectionItemViewType(position)) {
                    case SectionedRecyclerViewAdapter.VIEW_TYPE_HEADER:
                        return 2;
                    default:
                        return 1;
                }
            }
        });
        recyclerView.setLayoutManager(glm);
        recyclerView.setAdapter(sectionAdapter);




        Log.e("OwnerInfoTabFragment1","longitude : "+couponVO.getLongitude()+", latitude : "+couponVO.getLatitude());

        if(isFavoriteGetCountVO.isDangol()){
            heart.setImageResource(R.drawable.colorheart);
        }
        else{
            heart.setImageResource(R.drawable.blackheart);
        }
        heart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(Config.clientVO.getClient_key()!=0) {
                    if (isFavoriteGetCountVO.isDangol()) {
                        isFavoriteGetCountVO.setDangol_count(isFavoriteGetCountVO.getDangol_count() - 1);
                        isFavoriteGetCountVO.setDangol(false);
                        heart.setImageResource(R.drawable.blackheart);
                        countText.setText("" + isFavoriteGetCountVO.getDangol_count());
                    } else {
                        isFavoriteGetCountVO.setDangol_count(isFavoriteGetCountVO.getDangol_count() + 1);
                        isFavoriteGetCountVO.setDangol(true);
                        heart.setImageResource(R.drawable.colorheart);
                        countText.setText("" + isFavoriteGetCountVO.getDangol_count());
                    }
                    if(favBool != isFavoriteGetCountVO.isDangol()){
                        onFavSetListener.onFavSet(true); // 삭제해라
                    }
                    else
                        onFavSetListener.onFavSet(false); // 삭제하지마라
                }
                else{
                    if(Config.ownerVO.getOwner_key()!=0)
                        loginNeedPopupIntent.putExtra("down",1);
                    else
                        loginNeedPopupIntent.putExtra("down",0);
                    startActivity(loginNeedPopupIntent);
                }

            }
        });

        nameText.setText(couponVO.getMarket_name());
        introduceText.setText(couponVO.getMarket_introduce());

        return view;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMapContext =  new NMapContext(super.getActivity());
        mMapContext.onCreate();
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        NMapView mMapView = (NMapView)getView().findViewById(R.id.mapView);
        mMapView.setClientId(CLIENT_ID);// 클라이언트 아이디 설정
        mMapContext.setupMapView(mMapView); // fragment에서 사용할 거면 Context에 등록한 뒤 mapview 사용해야


        NMapController nMapController = mMapView.getMapController();
        mMapView.setClickable(true);
        mMapView.setEnabled(true);
        mMapView.setFocusable(true);
//        mMapView.setFocusableInTouchMode(true);
        mMapView.setScalingFactor(2.0F);
        Log.e("owenrInfoTabFragment1","longitude : "+couponVO.getLongitude()+", latitude : "+couponVO.getLatitude());
        nGeoPoint = new NGeoPoint(couponVO.getLongitude(), couponVO.getLatitude());
        nMapController.setMapCenter(nGeoPoint,11);
        mMapView.requestFocus();

        mMapView.setOnMapStateChangeListener(changeListener);
        mMapView.setOnMapViewTouchEventListener(mapListener);

//        setMarker();
        /*new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                setMarker();
            }
        }, 5000);*/



        nMapResourceProvider = new NMapViewerResourceProvider(getActivity());
        mapOverlayManager = new NMapOverlayManager(getActivity(), mMapView, nMapResourceProvider);

//        setMarker();
        int markerId = NMapPOIflagType.PIN;

        // POI data 설정
        NMapPOIdata poiData = new NMapPOIdata(2, nMapResourceProvider);
        poiData.beginPOIdata(1);
        poiData.addPOIitem(couponVO.getLongitude(), couponVO.getLatitude(), couponVO.getMarket_name(), markerId, 0);
        poiData.endPOIdata();

        // POI data overlay 생성
        NMapPOIdataOverlay poiDataOverlay = mapOverlayManager
                .createPOIdataOverlay(poiData, null);

        // POI data 표시, 해당 오버레이 객체레 포함된 전체 아이템이 화면에 표시되게 하려면 showAllPOIdata(0)
        // 메소드를 호출해야 합니다.
        poiDataOverlay.showFocusedItemOnly();

    }

    private NMapView.OnMapStateChangeListener changeListener = new NMapView.OnMapStateChangeListener() {
        @Override
        public void onMapInitHandler(NMapView nMapView, NMapError nMapError) {
            Log.e(TAG, "OnMapStateChangeListener onMapInitHandler : ");
        }


        @Override
        public void onMapCenterChange(NMapView nMapView, NGeoPoint nGeoPoint) {
            Log.e(TAG, "OnMapStateChangeListener onMapCenterChange : " + nGeoPoint.getLatitude() + " ㅡ  " + nGeoPoint.getLongitude());
        }

        @Override
        public void onMapCenterChangeFine(NMapView nMapView) {
            Log.e(TAG, "OnMapStateChangeListener onMapCenterChangeFine : ");
        }

        @Override
        public void onZoomLevelChange(NMapView nMapView, int i) {
            Log.e(TAG, "OnMapStateChangeListener onZoomLevelChange : " + i);
        }

        @Override
        public void onAnimationStateChange(NMapView nMapView, int i, int i1) {
            Log.e(TAG, "OnMapStateChangeListener onAnimationStateChange : ");
        }
    };

    private NMapView.OnMapViewTouchEventListener mapListener = new NMapView.OnMapViewTouchEventListener() {
        @Override
        public void onLongPress(NMapView nMapView, MotionEvent motionEvent) {
            Log.e(TAG, "OnMapViewTouchEventListener onLongPress : ");
        }

        @Override
        public void onLongPressCanceled(NMapView nMapView) {
            Log.e(TAG, "OnMapViewTouchEventListener onLongPressCanceled : ");
        }

        @Override
        public void onTouchDown(NMapView nMapView, MotionEvent motionEvent) {
            Log.e(TAG, "OnMapViewTouchEventListener onTouchDown : ");
            scrollView.requestDisallowInterceptTouchEvent(true);
        }

        @Override
        public void onTouchUp(NMapView nMapView, MotionEvent motionEvent) {
            Log.e(TAG, "OnMapViewTouchEventListener onTouchUp : ");
            scrollView.requestDisallowInterceptTouchEvent(false);
        }

        @Override
        public void onScroll(NMapView nMapView, MotionEvent motionEvent, MotionEvent motionEvent1) {
            Log.e(TAG, "OnMapViewTouchEventListener onScroll : ");
        }

        @Override
        public void onSingleTapUp(NMapView nMapView, MotionEvent motionEvent) {
            Log.e(TAG, "OnMapViewTouchEventListener onSingleTapUp : ");
        }
    };



    @Override
    public void onStart(){
        super.onStart();
        mMapContext.onStart();
    }
    @Override
    public void onResume() {
        super.onResume();
        mMapContext.onResume();
    }
    @Override
    public void onPause() {
        super.onPause();
        mMapContext.onPause();
    }
    @Override
    public void onStop() {
        mMapContext.onStop();
        super.onStop();
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapContext.onDestroy();
        allOwnerClientKeyVO = new AllOwnerClientKeyVO(couponVO.getOwner_key(), Config.clientVO.getClient_key());

        AllService service = Config.retrofit.create(AllService.class);
        if (favBool) {
            if (favBool != isFavoriteGetCountVO.isDangol()) {
                //단골삭제
                Call<String> request = service.deleteFavorite(allOwnerClientKeyVO);
                request.enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        Log.e("destroy", "OwnerInfoActivity의 http 코드 : " + response.code());
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        Log.e("destroy", "OwnerInfoActivity의 http 코드 : " );
                    }
                });
            }
        } else {
            if (favBool != isFavoriteGetCountVO.isDangol()) {
                //단골등록
                Call<String> request = service.insertFavorite(allOwnerClientKeyVO);
                request.enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        Log.e("destroy", "OwnerInfoActivity의 http 코드 : " + response.code());
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        Log.e("destroy", "OwnerInfoActivity의 http 코드 : " );
                    }
                });
            }
        }

        Log.e("destroy", "OwnerInfoActivity");

    }



    private class MovieSection extends StatelessSection {

        String title;
        List<MenuVO> list;

        MovieSection(String title, List<MenuVO> list) {
            super(SectionParameters.builder()
                    .itemResourceId(R.layout.section_ex5_item)
                    .headerResourceId(R.layout.section_ex5_header)
                    .build());

            this.title = title;
            this.list = list;
        }

        @Override
        public int getContentItemsTotal() {
            return list.size();
        }

        @Override
        public RecyclerView.ViewHolder getItemViewHolder(View view) {
            return new ItemViewHolder(view);
        }

        @Override
        public void onBindItemViewHolder(RecyclerView.ViewHolder holder, int position) {
            final ItemViewHolder itemHolder = (ItemViewHolder) holder;

            String name = list.get(position).getMenu_name();
            String money = list.get(position).getMenu_money();

            itemHolder.menuName.setText(name);
            itemHolder.menuMoney.setText(money);
            Glide.with(getActivity()).load(Config.url+"/drawable/owner/menu/"+couponVO.getOwner_key()+"/"+(position+1)+".png").into(itemHolder.menuImg);
        }

        @Override
        public RecyclerView.ViewHolder getHeaderViewHolder(View view) {
            return new HeaderViewHolder(view);
        }

        @Override
        public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder) {
            HeaderViewHolder headerHolder = (HeaderViewHolder) holder;

            headerHolder.tvTitle.setText(title);

        }
    }

    private class HeaderViewHolder extends RecyclerView.ViewHolder {

        private final TextView tvTitle;

        HeaderViewHolder(View view) {
            super(view);

            tvTitle = (TextView) view.findViewById(R.id.tvTitle);
        }
    }

    private class ItemViewHolder extends RecyclerView.ViewHolder {
        private final TextView menuName;
        private final TextView menuMoney;
        private final ImageView menuImg;

        ItemViewHolder(View view) {
            super(view);
            menuName = (TextView) view.findViewById(R.id.menuName);
            menuMoney = (TextView) view.findViewById(R.id.menuMoney);
            menuImg = (ImageView) view.findViewById(R.id.menuImg);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        onFavSetListener = (OnFavSetListener) context;
    }
    @Override
    public void onDetach() {
        super.onDetach();
        onFavSetListener = null;

    }



    public interface OnFavSetListener{
        void onFavSet(boolean fav);
    }


}