(function() {
    'use strict';

    angular
        .module('manateeApp')
        .controller('TeamDetailController', TeamDetailController);

    TeamDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Team'];

    function TeamDetailController($scope, $rootScope, $stateParams, entity, Team) {
        var vm = this;
        vm.team = entity;
        
        var unsubscribe = $rootScope.$on('manateeApp:teamUpdate', function(event, result) {
            vm.team = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();
