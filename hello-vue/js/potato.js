<!--  potato list -->

Date.prototype.plusHours = function(hours) {
  var mm = this.getMonth() + 1; // getMonth() is zero-based
  var dd = this.getDate() ;
  var hh = this.getHours() + hours;
  var mi = this.getMinutes();
  var ss = this.getSeconds();
  var off = this.getTimezoneOffset()/60;

  return [this.getFullYear(), "-",
          (mm>9 ? '' : '0') + mm, "-",
          (dd>9 ? '' : '0') + dd, "T",
          (hh>9 ? '' : '0') + hh, ":",
          (mi>9 ? '' : '0') + mi, ":",
          (ss>9 ? '' : '0') + ss
         ].join('');
};

var PotatoList = Vue.extend({
    template: '#potato-list',
    data() {
        return {
            searchKey: '',
            potatoes: [],
            errors: []
        }
    },
    mounted () {
        axios
            .get('./data/potatoes.json')
            .then(response => {
                this.potatoes = response.data;
                console.log(this.potatoes)
            })
            .catch(e => {
                this.errors.push(e);
            })
    }
});

var Potato = Vue.extend({
    template: '#potato',
});

var AddPotato = Vue.extend({
    template: '#add-potato',
    data: function () {

      var rightNow = new Date();
      var res = rightNow.plusHours(2);
        return {
            potato: {
                name: '',
                description: '',
                tags: '',
                priority: '0',
                estimation: '1h',
                deadline: res
            },
            errors: []
        }
    },
    methods: {
        createPotato: function() {
            console.log("createPotato: "+this.potato.name);
            
            //router.push('/');
        }
    }
});

var PotatoEdit = Vue.extend({
    template: '#edit-potato',

});

var PotatoDelete = Vue.extend({
    template: '#delete-potato',

});

var router = new VueRouter({routes:[
        { path: '/', component: PotatoList},
        { path: '/potatoes/:potato_id', component: Potato, title: 'Toast Potato'},
        { path: '/add-potato', component: AddPotato, title: 'Add Potato'},
        { path: '/potatoes/:potato_id/edit', component: PotatoEdit, title: 'note-edit'},
        { path: '/potatoes/:potato_id/delete', component: PotatoDelete, title: 'note-delete'}
    ]});

var app = new Vue({
    router:router
}).$mount('#app')