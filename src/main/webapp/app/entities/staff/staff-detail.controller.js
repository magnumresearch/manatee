(function() {
    'use strict';

    angular
        .module('manateeApp')
        .controller('StaffDetailController', StaffDetailController);

    StaffDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Staff', 'Team'];

    function StaffDetailController($scope, $rootScope, $stateParams, entity, Staff, Team) {
        var vm = this;
        vm.staff = entity;
        
        var unsubscribe = $rootScope.$on('manateeApp:staffUpdate', function(event, result) {
            vm.staff = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();
