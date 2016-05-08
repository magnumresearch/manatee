(function() {
    'use strict';

    angular
        .module('manateeApp')
        .controller('StaffController', StaffController);

    StaffController.$inject = ['$scope', '$state', 'Staff'];

    function StaffController ($scope, $state, Staff) {
        var vm = this;
        vm.staff = [];
        vm.loadAll = function() {
            Staff.query(function(result) {
                vm.staff = result;
            });
        };

        vm.loadAll();
        
    }
})();
