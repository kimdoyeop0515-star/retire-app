import React,{useRef,useEffect} from 'react';
import {StatusBar,View,BackHandler} from 'react-native';
import {WebView} from 'react-native-webview';
export default function App(){
  const ref=useRef(null);const canBack=useRef(false);
  useEffect(()=>{
    const h=BackHandler.addEventListener('hardwareBackPress',()=>{
      if(canBack.current&&ref.current){ref.current.goBack();return true;}
      return false;
    });
    return()=>h.remove();
  },[]);
  return(
    <View style={{flex:1,backgroundColor:'#07091a'}}>
      <StatusBar barStyle="light-content" backgroundColor="#07091a"/>
      <WebView ref={ref}
        source={require('./assets/app.html')}
        style={{flex:1,backgroundColor:'#07091a'}}
        javaScriptEnabled={true}
        domStorageEnabled={true}
        allowFileAccess={true}
        allowUniversalAccessFromFileURLs={true}
        mixedContentMode="always"
        onNavigationStateChange={s=>canBack.current=s.canGoBack}
      />
    </View>
  );
}
