(function() {
    'use strict';

    angular
        .module('manateeApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('queue', {
            parent: 'entity',
            url: '/queue',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Queues'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/queue/queues.html',
                    controller: 'QueueController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('queue-detail', {
            parent: 'entity',
            url: '/queue/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Queue'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/queue/queue-detail.html',
                    controller: 'QueueDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'Queue', function($stateParams, Queue) {
                    return Queue.get({id : $stateParams.id});
                }]
            }
        })
        .state('queue.new', {
            parent: 'queue',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/queue/queue-dialog.html',
                    controller: 'QueueDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                status: null,
                                timestampInitial: null,
                                timestampFinal: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('queue', null, { reload: true });
                }, function() {
                    $state.go('queue');
                });
            }]
        })
        .state('queue.edit', {
            parent: 'queue',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/queue/queue-dialog.html',
                    controller: 'QueueDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Queue', function(Queue) {
                            return Queue.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('queue', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('queue.delete', {
            parent: 'queue',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/queue/queue-delete-dialog.html',
                    controller: 'QueueDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Queue', function(Queue) {
                            return Queue.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('queue', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
