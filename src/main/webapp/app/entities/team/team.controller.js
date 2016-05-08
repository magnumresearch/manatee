(function() {
    'use strict';

    angular
        .module('manateeApp')
        .controller('TeamController', TeamController);

    TeamController.$inject = ['$scope', '$state', 'Team'];

    function TeamController ($scope, $state, Team) {
        var vm = this;
        vm.teams = [];
        vm.loadAll = function() {
            Team.query(function(result) {
                vm.teams = result;
            });
        };

        vm.loadAll();
        
    }
})();
