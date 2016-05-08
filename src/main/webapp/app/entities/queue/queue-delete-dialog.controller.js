(function() {
    'use strict';

    angular
        .module('manateeApp')
        .controller('QueueDeleteController',QueueDeleteController);

    QueueDeleteController.$inject = ['$uibModalInstance', 'entity', 'Queue'];

    function QueueDeleteController($uibModalInstance, entity, Queue) {
        var vm = this;
        vm.queue = entity;
        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        vm.confirmDelete = function (id) {
            Queue.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };
    }
})();
