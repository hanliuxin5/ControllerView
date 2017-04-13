<template>
  <div class="body">
      <controller style="width: 750px;flex:1;" :src="src" tag="lychee" ></controller>
      <div class="toolbar">
        <div class="toolbar_btn" @click="function_src(1)">1</div>
        <div class="toolbar_btn" @click="function_src(2)">2</div>
        <div class="toolbar_btn" @click="function_src(3)">3</div>
        <div class="toolbar_btn" @click="function_src(4)">4</div>
      </div>
  </div>
</template>
<script>
var globalEvent=weex.requireModule('globalEvent');

globalEvent.addEventListener("activity", function (result) {
        console.log("get activity");
  });

  export default {
    data () {
      return {
        src : 'http://192.168.0.110:8080/dist/fragment1.js'
      }
    },
    methods: {
      function_src : function(number){
        var _this = this;
        switch (number) {
          case 1:
          _this.src = 'http://192.168.0.110:8080/dist/fragment1.js';
            break;
          case 2:
            _this.src = 'http://192.168.0.110:8080/dist/fragment2.js';
            break;
          case 3:
            _this.src = 'http://192.168.0.110:8080/dist/fragment3.js';
            break;
          case 4:
          _this.src = 'http://192.168.0.110:8080/dist/fragment4.js';
            break;
        }
      }
    }
  }
</script>
<style>
.body{
  flex-direction:  column;
}
.toolbar{
  background-color: #EEE;
  display: flex;
  justify-content: center;
  align-items: center;
  flex-direction: row;
  width: 750px;
  height: 80px;
}
.toolbar_btn{
  flex: 1;
  text-align: center;
  align-items: center;
  height: 80px;
  justify-content: center;
}
.toolbar_btn:active{
  background-color: #999;
  height: 80px;
}
</style>
