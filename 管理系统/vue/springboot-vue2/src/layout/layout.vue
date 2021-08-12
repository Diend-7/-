<template>
    <div>
      <Header></Header>
      <div style="display: flex">
        <Aside></Aside>
        <RouterView style="flex: 1"></RouterView>
      </div>
    </div>
</template>

<script>
   import Header from "../components/Header";
   import Aside from "../components/Aside";
   export default {
    name: 'Layout',
    components: {Aside, Header}
  }
</script>

<style scoped>

</style>
