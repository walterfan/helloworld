
var PotatoAdd = Vue.extend({
  template: '#potato-add',
  data: function () {
    return { potato: {name: '', description: '', priority:1, deadline: ''}}
  },
  methods: {
    createPotato: function() {
      var  v = this.potato;
      potatoes.push({
        id: Math.random().toString().split('.')[1],
        name: potato.name,
        description: potato.description,
        priority: potato.tag,
        deadline: potato.deadline
      });
      router.push('/');
    }
  }
});

var Potato = Vue.extend({

});

var PotatoEdit = Vue.extend({

});

var PotatoDelete = Vue.extend({

});

var router = new VueRouter({routes:[
  { path: '/', component: List},
  { path: '/potatoes/:id', component: Potato, title: 'Potato'},
  { path: '/potatoes/add', component: PotatoAdd},
  { path: '/potatoes/:id/edit', component: PotatoEdit, title: 'Potato Edit'},
  { path: '/potatoes/:id/delete', component: PotatoDelete, title: 'Potato Delete'}
]});

var app = new Vue({
  router:router
}).$mount('#app')