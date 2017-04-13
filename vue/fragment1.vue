<template>
  <div class="wrapper">
      <image src="https://alibaba.github.io/weex/img/weex_logo_blue@3x.png" class="logo"></image>
      <text class="button" @click="update">1111 Hello {{target}}</text>
  </div>
</template>



<script>
  var globalEvent=weex.requireModule('globalEvent');
  globalEvent.addEventListener("fragment", function (result) {
          console.log("get fragment");
    });

  export default {
    data: {
      target: 'World'
    },
    methods: {
      update: function () {
        this.target = 'Lychee';
        test.showToast();
      }
  }
}
</script>

<style>
  .wrapper { align-items: center;background-color:#66ccff;}
  .button {
      font-size: 48px;
      margin-top: 8px;
      background-color: black;
      color: red;
      padding: 8px;
        }
  .logo { width: 360px; height: 82px; margin-top: 40px;margin-bottom: 40px;background-color:#66cc00;}
</style>
