package com.fengnian.smallyellowo.foodie;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import com.fan.framework.base.MyBaseAdapter;
import com.fan.framework.imageloader.FFImageLoader;
import com.fengnian.smallyellowo.foodie.appbase.BaseActivity;
import com.fengnian.smallyellowo.foodie.appconfig.Constants;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class Main3Activity extends BaseActivity {

    String[] avatarUrls = {"http://smallyellow.tinydonuts.cn/14dc779c8ed23c92d2c635c5d289fd47.jpg",
            "http://smallyellow.tinydonuts.cn/0306d4e296ebc5110549a49b6fe4417c.jpg",
            "http://smallyellow.tinydonuts.cn/066f36a34640edba766fcd87ce4865a2.jpg",
            "http://smallyellow.tinydonuts.cn/ef72531ce594810b19aab2c2ea57402b.jpg",
            "http://smallyellow.tinydonuts.cn/066f36a34640edba766fcd87ce4865a2.jpg",
            "http://smallyellow.tinydonuts.cn/6c3dce01cd349d98883fe89970d1be8c.jpg",
            "http://smallyellow.tinydonuts.cn/aa1eced37722bebffb17d58eb24ff85c.jpg",
            "http://smallyellow.tinydonuts.cn/c685475ec787c678c93e3a7ddea19821.jpg",
            "http://smallyellow.tinydonuts.cn/c685475ec787c678c93e3a7ddea19821.jpg",
            "http://smallyellow.tinydonuts.cn/f78696c926a6251388b0805f25f6acb0.jpg",
            "http://smallyellow.tinydonuts.cn/f78696c926a6251388b0805f25f6acb0.jpg",
            "http://smallyellow.tinydonuts.cn/7f448aba14ed15ac4ac4e2ef165a4bcc.jpg",
            "http://smallyellow.tinydonuts.cn/2ee64216355ca99f2ffda8099c130d3d.jpg",
            "http://smallyellow.tinydonuts.cn/9dc4fd86075832169cd1affaab8415c4.jpg",
            "http://smallyellow.tinydonuts.cn/9a334ffdd5d07623e988d8c0e3b0bd89.jpg",
            "http://smallyellow.tinydonuts.cn/8c12bf4581a30b5a33fe374293e53604.jpg",
            "http://smallyellow.tinydonuts.cn/3a7efd93af3683bc9b6ba8f2bd2120a0.jpg",
            "http://smallyellow.tinydonuts.cn/17a63dbed8c4551058178f94c11e3615.jpg",
            "http://smallyellow.tinydonuts.cn/9dc4fd86075832169cd1affaab8415c4.jpg",
            "http://smallyellow.tinydonuts.cn/2c39d36cc5cd3b9c00ded2233e77e16c.jpg",
            "http://smallyellow.tinydonuts.cn/1aacac1a6b0a084be3d00a498f2a6d1c.jpg",
            "http://smallyellow.tinydonuts.cn/ed3bdbc13e1073efdde9a1241c426644.jpg",
            "http://smallyellow.tinydonuts.cn/b667f42330465fbd4ddf360b4c648ad5.jpg",
            "http://smallyellow.tinydonuts.cn/fb79a7fba5301d0ee5bcfe7535abc9dd.jpg",
            "http://smallyellow.tinydonuts.cn/52b0d57877cfb78edfb396595e61e07d.jpg",
            "http://smallyellow.tinydonuts.cn/20426bf20eec46d1bb815bc030c2f928.jpg",
            "http://smallyellow.tinydonuts.cn/bda6a31edf9860f04affb15635e8bae9.jpg",
            "http://smallyellow.tinydonuts.cn/41c7e4756804b1e015aa509c095c8aa8.jpg",
            "http://smallyellow.tinydonuts.cn/4112f65a22ede9f0082de84c473a398e.jpg",
            "http://smallyellow.tinydonuts.cn/41c7e4756804b1e015aa509c095c8aa8.jpg",
            "http://smallyellow.tinydonuts.cn/846e06292c14e2651fd8e87a6baaf9c0.jpg",
            "http://smallyellow.tinydonuts.cn/f0b9a522882785a6293357b2278679ed.jpg",
            "http://smallyellow.tinydonuts.cn/6bce31288971002a34108684871da996.jpg",
            "http://smallyellow.tinydonuts.cn/8468d22a2abf17f4a1640f30c2bed219.jpg",
            "http://smallyellow.tinydonuts.cn/7266170093326925ed8771d1ea70b1ae.jpg",
            "http://smallyellow.tinydonuts.cn/529f74baa820959a6fa1cd6031bba3c9.jpg",
            "http://smallyellow.tinydonuts.cn/f50d96d44c8307efa83de169d0a066f9.jpg",
            "http://smallyellow.tinydonuts.cn/48063a8769bd3bee6e2504c9a41427a7.jpg",
            "http://smallyellow.tinydonuts.cn/214be39a5a7ee61bb5d481dd4fa4365c.jpg",
            "http://smallyellow.tinydonuts.cn/21bbff119699fe74b8bc8c8fee002edc.jpg",
            "http://smallyellow.tinydonuts.cn/96961c2f30bd7ff862a173a8d9d31108.jpg",
            "http://smallyellow.tinydonuts.cn/cb4c96e2e60f0fe262dda4c33ec3daf5.jpg",
            "http://smallyellow.tinydonuts.cn/ed3bdbc13e1073efdde9a1241c426644.jpg",
            "http://smallyellow.tinydonuts.cn/f7286dfcd1b8021a3ce5d82292bc6fe8.jpg",
            "http://smallyellow.tinydonuts.cn/8468d22a2abf17f4a1640f30c2bed219.jpg",
            "http://smallyellow.tinydonuts.cn/8468d22a2abf17f4a1640f30c2bed219.jpg",
            "http://smallyellow.tinydonuts.cn/8468d22a2abf17f4a1640f30c2bed219.jpg",
            "http://smallyellow.tinydonuts.cn/aa455478c50cc004d5c3e9281bf00089.jpg",
            "http://smallyellow.tinydonuts.cn/aa455478c50cc004d5c3e9281bf00089.jpg",
            "http://smallyellow.tinydonuts.cn/ff881da4243b27031e9329cdc9d8e693.jpg",
            "http://smallyellow.tinydonuts.cn/ff881da4243b27031e9329cdc9d8e693.jpg",
            "http://smallyellow.tinydonuts.cn/aa2f05d939b44b876aad6da09a6f9c70.jpg",
            "http://smallyellow.tinydonuts.cn/67252e61a5548231aa8fbda6a941a2de.jpg",
            "http://smallyellow.tinydonuts.cn/6ae5b073429924bf2225215c201cfd85.jpg",
            "http://smallyellow.tinydonuts.cn/a460d54877862a1fed9e916c9630a685.jpg",
            "http://smallyellow.tinydonuts.cn/b441da57756b2a6f07a645e82c7313ae.jpg",
            "http://smallyellow.tinydonuts.cn/3054120a1e0414fa545c57192d87850f.jpg",
            "http://smallyellow.tinydonuts.cn/21f4aa4c3c92a6ae8aed7b0c2f1dfbec.jpg",
            "http://smallyellow.tinydonuts.cn/21f4aa4c3c92a6ae8aed7b0c2f1dfbec.jpg",
            "http://smallyellow.tinydonuts.cn/21f4aa4c3c92a6ae8aed7b0c2f1dfbec.jpg",
            "http://smallyellow.tinydonuts.cn/eaea958ce80081a890f3138532027884.jpg",
            "http://smallyellow.tinydonuts.cn/4978202957b3f60df84c6aa7ec353ebb.jpg",
            "http://smallyellow.tinydonuts.cn/b441da57756b2a6f07a645e82c7313ae.jpg",
            "http://smallyellow.tinydonuts.cn/9ab9c1f6471da210386ab81663be5245.jpg",
            "http://smallyellow.tinydonuts.cn/647d027d1d6eaf72d75a3aaabc7ddce5.jpg",
            "http://smallyellow.tinydonuts.cn/67fab7bae3cb2abf75c73f33b9558e1a.jpg",
            "http://smallyellow.tinydonuts.cn/9ab9c1f6471da210386ab81663be5245.jpg",
            "http://smallyellow.tinydonuts.cn/a2c130c78a2e9318620db05f8e11068c.jpg",
            "http://smallyellow.tinydonuts.cn/a460d54877862a1fed9e916c9630a685.jpg",
            "http://smallyellow.tinydonuts.cn/34d8d09eb5d8964af7440bd1bcf58a01.jpg",
            "http://smallyellow.tinydonuts.cn/9babfe43e3043d2b85cd4ca4415587dc.jpg",
            "http://smallyellow.tinydonuts.cn/2c792557d2957b2d50fedb1b3a6c63d5.jpg",
            "http://smallyellow.tinydonuts.cn/b0f8c90652d0147d44de238fd02326b2.jpg",
            "http://smallyellow.tinydonuts.cn/45c04b5f48a73841450fd02bf6d34544.jpg",
            "http://smallyellow.tinydonuts.cn/f8e0a703586fd999f8b824696bf61143.jpg",
            "http://smallyellow.tinydonuts.cn/28378f8a3974539802680a12a9336959.jpg",
            "http://smallyellow.tinydonuts.cn/ec96636025283622c2fe454626f18995.jpg",
            "http://smallyellow.tinydonuts.cn/8f6bff7c265c70706512644713c55bbd.jpg",
            "http://smallyellow.tinydonuts.cn/1946b3f877d2a4bf328823f03eb3a557.jpg",
            "http://smallyellow.tinydonuts.cn/1009ce1449860907dcb13fff806506e2.jpg",
            "http://smallyellow.tinydonuts.cn/0dc5e70b163537657b781b37db96f2b3.jpg",
            "http://smallyellow.tinydonuts.cn/c4c85f323e3fb727619abf3574bb0f51.jpg",
            "http://smallyellow.tinydonuts.cn/87e76c72d9d55eab64efac2875658f84.jpg",
            "http://smallyellow.tinydonuts.cn/b589702a42f0ec21913c44f8f9f75969.jpg",
            "http://smallyellow.tinydonuts.cn/67fab7bae3cb2abf75c73f33b9558e1a.jpg",
            "http://smallyellow.tinydonuts.cn/0895923983da19561954c2cc3ec6d8e4.jpg",
            "http://smallyellow.tinydonuts.cn/09dd7ba4b00a4e16c35b4ec86c8a3fa0.jpg",
            "http://smallyellow.tinydonuts.cn/f4f7907bfeb71a56d93a50689039d24f.jpg",
            "http://smallyellow.tinydonuts.cn/d288ee1954e803c47a390a843fd33eb7.jpg",
            "http://smallyellow.tinydonuts.cn/d288ee1954e803c47a390a843fd33eb7.jpg",
            "http://smallyellow.tinydonuts.cn/9fa1bf2e3389542cf0ab3626de0ddbfc.jpg",
            "http://smallyellow.tinydonuts.cn/4cb8a311ddeb27eacb7090e9032242b2.jpg",
            "http://smallyellow.tinydonuts.cn/29e30ffc7d5020cf7fad4ded81213bf1.jpg",
            "http://smallyellow.tinydonuts.cn/c974bebfa3e947597fc72990562cc759.jpg",
            "http://smallyellow.tinydonuts.cn/a9e23c1259ba2681bd8400c7ab4a8cbb.jpg",
            "http://smallyellow.tinydonuts.cn/e682bc16749df46c0ddd08c65a7e266d.jpg",
            "http://smallyellow.tinydonuts.cn/529c7c784a6296bcca48cd3598d2b09b.jpg",
            "http://smallyellow.tinydonuts.cn/a3c7604336878218589c248d5f23e6f3.jpg",
            "http://smallyellow.tinydonuts.cn/45bf0c42160d69e6142af81fe861e412.jpg",
            "http://smallyellow.tinydonuts.cn/2ba9cbdc2227971f1aabe76d1ca4b62c.jpg"};
    @Bind(R.id.listView)
    ListView listView;
    private MyBaseAdapter<Holder, String> adapter;

    public static class Holder {
//        ImageView iv1;
//        SimpleDraweeView iv5;
//        ImageView iv3;
//        SimpleDraweeView iv4;
//        SimpleDraweeView iv2;
    }

    int size1 = Constants.MiddleImage;

    int size = Constants.MiddleImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        ButterKnife.bind(this);
        ArrayList<String> list = new ArrayList<>();
        for (int i = 0; i< avatarUrls.length;i++){
            list.add(avatarUrls[i]+"@" + size + "w_" + size + "h_80Q_2o");
        }
        adapter = new MyBaseAdapter<Holder, String>(Holder.class, this, R.layout.item_imgs_test, list) {
            @Override
            public void initView(View convertView, Holder holder, int position, String item) {
//                RoundingParams roundingParams = RoundingParams.fromCornersRadius(5f);
//                roundingParams.setBorder(0, 1.0f);
//                roundingParams.setRoundAsCircle(true);
//                holder.iv4.getHierarchy().setRoundingParams(roundingParams);
//                holder.iv5.setImageURI(item_image_gallery);
//                holder.iv4.setImageURI(item_image_gallery);
//M
//
////                Picasso.with(context()).load(item_image_gallery).noFade().into(holder.iv1);
////                holder.iv2.setImageURI(item_image_gallery);
////                holder.iv5.setImageURI(item_image_gallery);
//                FFImageLoader.loadMiddleImage(context(), item_image_gallery, holder.iv1);
//                FFImageLoader.load_base(context(), item_image_gallery, holder.iv3, true, size1, size1, R.drawable.alpha, FFImageLoader.TYPE_ROUND, null);
//                holder.iv5.set
            }
        };
//        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl("http://106.3.227.33/pulamsi/")
//                //增加返回值为String的支持
//                .addConverterFactory(ScalarsConverterFactory.create())
//                //增加返回值为Gson的支持(以实体类返回)
//                .addConverterFactory(GsonConverterFactory.create())
////                //增加返回值为Oservable<T>的支持
////                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
//                .build();
//        retrofit.
        listView.setAdapter(adapter);
    }

    @Override
    public void close(View v) {
        size++;
        ArrayList<String> list = new ArrayList<>();
        for (int i = 0; i< avatarUrls.length;i++){
            list.add(avatarUrls[i]+"@" + size + "w_" + size + "h_80Q_2o");
        }
        adapter.setData(list);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onBackPressed(View v) {
        adapter.notifyDataSetChanged();
    }
}
